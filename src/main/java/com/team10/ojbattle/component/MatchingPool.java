package com.team10.ojbattle.component;

import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Question;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.exception.MyErrorCodeEnum;
import com.team10.ojbattle.exception.MyException;
import com.team10.ojbattle.service.GameService;
import com.team10.ojbattle.service.QuestionService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: 陈健航
 * @description: 匹配池
 * @since: 2020/4/17 12:11
 * @version: 1.0
 */
@Component
public class MatchingPool {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GameService gameService;

    /**
     * redis匹配池的完整key,
     */
    private static final String WAIT_FOR_KEY = "BATTLE_MATCH_POOL";

    /**
     * redis锁用的key
     */
    private static final String MATCH_LOCK_KEY = "MATCH_LOCK_KEY";

    /**
     * 因为匹配池的zset无法实现成员过期，所以要加点辅助key
     */
    private static final String MATCH_EXPIRE_KEY = "MATCH_EXPIRE:";

    /**
     * 通知对手的id，后面要加上battleId
     */
    private static final String IS_MATCH_KEY = "BATTLE_IS_MATCH:";

    /**
     * 表示自己还在对局的key,后面加自己的id
     */
    private static final String ON_GAME_KEY = "BATTLE_ON_GAME:";

    /**
     * 返回值：作为被匹配方接受到了匹配
     */
    public static final String RET_HAVE_MATCH = "RET_HAVE_MATCH";

    /**
     * 返回值：匹配池数量不够，继续轮询
     */
    public static final String RET_KEEP_MATCH = "RET_KEEP_MATCH";

    /**
     * 返回值：作为发起方匹配到其他用户
     */
    public static final String RET_MATCH_OTHER = "RET_MATCH_OTHER";

    /**
     * 返回值：对方未确认对局，继续确认
     */
    public static final String RET_KEEP_CONFIRM = "RET_KEEP_CONFIRM";

    /**
     * 返回值：第三次握手错误，此时应该重新加入匹配池
     */
    public static final String RET_MATCH_ERROR = "RET_MATCH_ERROR";

    /**
     * MAP_KEY_OPPONENT_ID
     */
    private static final String MAP_KEY_OPPONENT_ID = "opponentId";

    /**
     * MAP_KEY_OPPONENT_NAME
     */
    private static final String MAP_KEY_OPPONENT_NAME = "opponentName";

    /**
     * 加入匹配池，需要加锁
     */
    @Klock(keys = MATCH_LOCK_KEY)
    public void match() {
        //获取用户信息
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        String username = authUser.getUsername();
        Integer ranking = authUser.getRanking();
        String userData = userId + "_" + username;

        //删除可能存在的gameKey
        stringRedisTemplate.delete(ON_GAME_KEY + userData);

        //把自己扔进匹配池
        stringRedisTemplate.opsForZSet().add(WAIT_FOR_KEY, userData, ranking);

        //zset成员不能设过期时间，另外用一个键值对辅助。有效时间1分钟
        stringRedisTemplate.opsForValue().set(MATCH_EXPIRE_KEY + userData, "1", 15, TimeUnit.SECONDS);
    }

    /**
     * 第一次握手,加锁
     *
     * @return 结果集，res：RET_HAVE_MATCH自己已经被匹配/RET_KEEP_MATCH匹配池数量不足/RET_MATCH_OTHER匹配到其他人/
     */
    @Klock(keys = MATCH_LOCK_KEY)
    public Map<String, String> firstShakeHand() {
        //取出必要信息和定义结果集
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        String username = authUser.getUsername();
        String userData = userId + "_" + username;
        Map<String, String> res = new HashMap<>(3);
        //查询自己有没有被匹配,rank，为空代表自己经被匹配
        Long rank = stringRedisTemplate.opsForZSet().rank(WAIT_FOR_KEY, userData);
        if (rank == null) {
            //自己已经被匹配
            res.put("res", RET_HAVE_MATCH);
            return res;
        }

        //查看匹配池长度是否大于1。
        Long size = stringRedisTemplate.opsForZSet().size(WAIT_FOR_KEY);
        if (size == null || size <= 1) {
            //池中数量不足，自己也没有被匹配，但是仍然要刷新时间，表示自己仍在匹配池中
            stringRedisTemplate.opsForValue().set(MATCH_EXPIRE_KEY + userData, "1", 15, TimeUnit.SECONDS);
            //抛出异常匹配池数量不足
            res.put("res", RET_KEEP_MATCH);
            return res;
        }

        //开始匹配其他人，默认往后匹配，例如池中ranking为，11，12，20，25.则12匹配20，或者20匹配25。
        //如果是池中末尾就特殊地往前匹配，例如25匹配20。
        //对战的信息
        boolean keepMatching;
        String opponentData = null;
        do {
            Set<String> range;
            //用户为池中最后一个
            if (rank + 1 == size) {
                range = stringRedisTemplate.opsForZSet().range(WAIT_FOR_KEY, rank, rank - 1);
            } else {
                range = stringRedisTemplate.opsForZSet().range(WAIT_FOR_KEY, rank, rank + 1);
            }
            if (range != null) {
                //取出set的元素，该set应该只有一个
                opponentData = Arrays.asList(range.toArray(new String[0])).get(0);
            }

            //取出后还要判断这个用户时候还有效，即排除加入匹配池却没有更新轮询状态的（点击了匹配，然后没有开始游戏就关闭了窗口）
            String exist = stringRedisTemplate.opsForValue().get(MATCH_EXPIRE_KEY + opponentData);

            //是个无效用户，删除
            if (StringUtil.isNullOrEmpty(exist)) {
                //删除该无效用户
                stringRedisTemplate.opsForZSet().remove(WAIT_FOR_KEY, opponentData);
                //清空
                opponentData = null;
                size = stringRedisTemplate.opsForZSet().size(WAIT_FOR_KEY);

                //匹配池长度仍然大于1，继续匹配。匹配池数量不足就不匹配了
                keepMatching = size != null && size > 1;
            } else {
                //匹配到了，在连接池中删除自己和对手的信息
                stringRedisTemplate.opsForZSet().remove(WAIT_FOR_KEY, userData);
                stringRedisTemplate.opsForZSet().remove(WAIT_FOR_KEY, opponentData);
                keepMatching = false;
            }
        } while (keepMatching);

        //匹配池数量不够而跳出循环，返回匹配中错误码
        if (opponentData == null) {
            //池中数量不足，自己也没有被匹配，但是仍然要刷新时间，表示自己仍在匹配池中
            stringRedisTemplate.opsForValue().set(MATCH_EXPIRE_KEY + userData, "1", 15, TimeUnit.SECONDS);
            res.put("res", RET_KEEP_MATCH);
            return res;
        }

        String[] temp = opponentData.split("_");
        String opponentId = temp[0];
        String opponentName = temp[1];
        res.put(MAP_KEY_OPPONENT_ID, opponentId);
        res.put(MAP_KEY_OPPONENT_NAME, opponentName);
        res.put("res", RET_MATCH_OTHER);
        //利用redis通知对方我已经匹配到你了，请对方创建对局，然后我通过查询redis来confim,当对方确认后，value变成gameId，
        //如果该值过期，表示对方没有确认，重新进入匹配
        stringRedisTemplate.opsForValue().set(IS_MATCH_KEY + opponentData, userData, 15, TimeUnit.SECONDS);
        return res;
    }

    /**
     * 第二次握手，没有操作公共资源，不需要握手,有抛出异常。没有要特殊处理的
     *
     * @return 新建的对局id
     */
    public Long secondShakeHand() {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        String username = authUser.getUsername();
        String userData = userId + "_" + username;
        //保存对局
        Game game = new Game();
        game.setType(1);
        game.setPlayer1Id(userId);
        game.setPlayer1Username(username);
        //随机取出一条题目
        Question question = questionService.selectOneByRandom();
        game.setQuestionTitle(question.getTitle());
        game.setQuestionId(question.getId());
        gameService.save(game);

        //通知对方我已经确认了对局，并且开启了对局
        String opponentData = stringRedisTemplate.opsForValue().getAndSet(IS_MATCH_KEY + userData, String.valueOf(game.getId()));

        //现在回填信息是因为避免redis并发导致的问题
        if (opponentData == null) {
            throw new MyException(MyErrorCodeEnum.MATCH_ERROR);
        }
        String[] temp = opponentData.split("_");
        Long opponentId = Long.parseLong(temp[0]);
        String opponentName = temp[1];
        game.setPlayer2Id(opponentId);
        game.setPlayer2Username(opponentName);
        gameService.updateById(game);
        return game.getId();
    }

    /**
     * @param map
     * @return gameId对局id/RET_KEEP_CONFIRM轮询确认/RET_MATCH_ERROR匹配错误
     */
    public String thirdShakeHand(Map<String, String> map) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        String username = authUser.getUsername();
        String userData = userId + "_" + username;

        String opponentData = map.get(MAP_KEY_OPPONENT_ID) + "_" + map.get(MAP_KEY_OPPONENT_ID);
        String gameId = stringRedisTemplate.opsForValue().get(IS_MATCH_KEY + opponentData);
        //key过期，可能对方已经退出了匹配，没有进行确认，此时应该重新加入匹配池。
        if(StringUtil.isNullOrEmpty(gameId)) {
            return RET_MATCH_ERROR;
        }
        if (userData.equals(gameId)) {
            //value没有改变，对方还没有确认
            return RET_KEEP_CONFIRM;
        }
        //取出来的是gameId，对方已经开启对局
        return gameId;
    }
}
