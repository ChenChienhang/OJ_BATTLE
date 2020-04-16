package com.team10.ojbattle.service.impl;

import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Question;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.exception.MyErrorCodeEnum;
import com.team10.ojbattle.exception.MyException;
import com.team10.ojbattle.service.BattleService;
import com.team10.ojbattle.service.GameService;
import com.team10.ojbattle.service.QuestionService;
import com.team10.ojbattle.service.SysUserService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/4/7 21:59
 * @version: 1.0
 */
@Service("battleService")
public class BattleServiceImpl implements BattleService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    GameService gameService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    QuestionService questionService;

    /**
     * redis匹配队列的完整key,
     */
    private static final String WAIT_FOR_KEY = "BATTLE_WAIT_MATCH";

    /**
     * redis匹配用的key
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
     * 还在对局中value
     */
    private static final String ON_GAME_VALUE = "ON_GAME:";

    /**
     * 提交value
     */
    private static final String PASS_GAME_VALUE = "PASS_GAME";


    @Klock(keys = MATCH_LOCK_KEY)
    @Override
    public void battleMatch() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();
        String username = authUser.getUsername();
        Integer ranking = authUser.getRanking();
        String userData = userId + "_" + username;
        //删除可能存在的gameKey
        stringRedisTemplate.delete(ON_GAME_KEY + userData);
        //把自己扔进匹配池
        stringRedisTemplate.opsForZSet().add(WAIT_FOR_KEY, userData, ranking);
        //zset成员不能设过期时间，另外用一个键值对辅助。
        stringRedisTemplate.opsForValue().set(MATCH_EXPIRE_KEY + userData, "1", 15, TimeUnit.SECONDS);
    }


    /**
     *
     * @return 创建好的游戏对局id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String confirm() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();
        String username = authUser.getUsername();
        String userData = userId + "_" + username;
        //保存对局
        Game game = new Game();
        game.setType("1");
        game.setStartTime(new Date());
        game.setPlayer1Id(userId);
        game.setPlayer1Username(username);
        //随机取出一条题目
        Question question = questionService.selectOneByRandom();
        game.setQuestionTitle(question.getTitle());
        game.setQuestionId(question.getId());
        gameService.save(game);

        //通知对方我已经确认了对局，并且开启了对局
        String opponentData = stringRedisTemplate.opsForValue().getAndSet(IS_MATCH_KEY + userData, game.getId());

        //现在回填信息是因为避免redis并发导致的问题
        assert opponentData != null;
        String[] temp = opponentData.split("_");
        String opponentId = temp[0];
        String opponentName = temp[1];
        game.setPlayer2Id(opponentId);
        game.setPlayer2Username(opponentName);
        gameService.updateById(game);
        return game.getId();
    }

    /**
     * 等待确认，可能等待确认异常
     * @param map 对手id，对手name
     * @return 对局id
     */
    @Override
    public String waitConfirm(Map<String, String> map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();
        String username = authUser.getUsername();
        String userData = userId + "_" + username;

        String opponentData = map.get("opponentId") + "_" + map.get("opponentName");
        String gameId = stringRedisTemplate.opsForValue().get(IS_MATCH_KEY + opponentData);
        if (userData.equals(gameId)) {
            //value没有改变，对方还没有确认
            throw new MyException(MyErrorCodeEnum.WAIT_CONFIRM_ERROR);
        }
        //取出来的是gameId，对方已经开启对局
        return gameId;
    }

    /**
     * 返回结果：1.自己已经被别人匹配，2.匹配池数量不足，3.自己匹配了别人
     *
     * @return
     */
    @Klock(keys = MATCH_LOCK_KEY)
    @Override
    public Map<String, String> waitForMatching() {
        //取出必要信息和定义结果集
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();
        String username = authUser.getUsername();
        String userData = userId + "_" + username;
        Map<String, String> res = new HashMap<>(2);

        //查询自己有没有被匹配,rank，为空代表自己经被匹配
        Long rank = stringRedisTemplate.opsForZSet().rank(WAIT_FOR_KEY, userData);
        if (rank == null) {
            //自己已经被匹配
            throw new MyException(MyErrorCodeEnum.CONFIRM_ERROR);
        }

        //查看匹配池长度是否大于1。
        Long size = stringRedisTemplate.opsForZSet().size(WAIT_FOR_KEY);
        if (size == null || size <= 1) {
            //池中数量不足，自己也没有被匹配，但是仍然要刷新时间，表示自己仍在匹配池中
            stringRedisTemplate.opsForValue().set(MATCH_EXPIRE_KEY + userData, "1", 15, TimeUnit.SECONDS);
            //抛出异常匹配池数量不足
            throw new MyException(MyErrorCodeEnum.KEEP_MATCHING_ERROR);
        }

        //开始匹配其他人，默认往后匹配，例如池中ranking为，11，12，20，25.则12匹配20，或者20匹配25。
        //如果是池中末尾就特殊地往前匹配，例如25匹配20。
        //对战的信息
        boolean keepMatching;
        String opponentData = null;
        do {
            Set<String> range;
            //最后一个
            if (rank + 1 == size) {
                range = stringRedisTemplate.opsForZSet().range(WAIT_FOR_KEY, rank, rank - 1);
            } else {
                range = stringRedisTemplate.opsForZSet().range(WAIT_FOR_KEY, rank, rank + 1);
            }
            if (range != null) {
                //取出set的元素，应该只有一个
                opponentData = Arrays.asList(range.toArray(new String[0])).get(0);
            }

            //取出后还要判断这个用户时候还有效，即排除加入匹配池却没有更新轮询状态的（点击了匹配，然后没有开始游戏就关闭了窗口）
            String exist = stringRedisTemplate.opsForValue().get(MATCH_EXPIRE_KEY + opponentData);

            //是个无效用户，删除
            if (exist == null) {
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
            throw new MyException(MyErrorCodeEnum.KEEP_MATCHING_ERROR);
        }

        String[] temp = opponentData.split("_");
        String opponentId = temp[0];
        String opponentName = temp[1];
        res.put("opponentId", opponentId);
        res.put("opponentName", opponentName);
        //利用redis通知对方我已经匹配到你了，请对方创建对局，然后我通过查询redis来confim,当对方确认后，value变成gameId，
        //如果该值过期，表示对方没有确认，重新进入匹配
        stringRedisTemplate.opsForValue().set(IS_MATCH_KEY + opponentData, userData, 10, TimeUnit.SECONDS);
        return res;
    }

    @Override
    public void heartBeat(String opponentId) {
        //获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();

        //没有并发问题，不用事务
        //表示自己还在游戏中，有效时间2min
        stringRedisTemplate.opsForValue().set(ON_GAME_KEY + userId, ON_GAME_VALUE, 2, TimeUnit.MINUTES);

        //查询对方是否还在对局中
        String opponentValue = stringRedisTemplate.opsForValue().get(ON_GAME_KEY + opponentId);

        //对方不在对局中，已经放弃或者离线
        if (opponentValue == null) {
            throw new MyException(MyErrorCodeEnum.QUIT_ERROR);
        } else {
            //对方还在对局，且已经完成对局
            if (PASS_GAME_VALUE.equals(opponentValue)) {
                throw new MyException(MyErrorCodeEnum.PASS_ERROR);
            }
            //否则表示对方还在做题，直接结束
        }
    }

    @Override
    public void submit(Submission submission) {
    }

    /**
     * 退出对局，删除对局
     */
    @Override
    public void quit() {
        //获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();
        //删除自身对局信息，表示已退出
        stringRedisTemplate.delete(ON_GAME_KEY + userId);
    }


}
