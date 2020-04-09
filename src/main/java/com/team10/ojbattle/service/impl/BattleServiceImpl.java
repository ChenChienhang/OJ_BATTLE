package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.exception.MyErrorCodeEnum;
import com.team10.ojbattle.exception.MyException;
import com.team10.ojbattle.service.BattleService;
import com.team10.ojbattle.service.GameService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    private final String WAIT_FOR_KEY = "battle_wait_match";

    private final String IS_MATCH_KEY = "battle_match_";

    private final String ON_GAME_KEY = "battle_on_game_";

    /**
     * 还在对局中value
     */
    private final String ON_GAME_VALUE = "ON_GAME";

    /**
     * 提交value
     */
    private final String PASS_GAME_VALUE = "PASS_GAME";


    @Override
    public Game battleMatch() {
        //涉及到redis事务，尽量不要用抛异常
        //取出用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();
        String username = authentication.getName();
        System.out.println(userId);
        System.out.println(username);
        boolean battleFlag = false;
        String gameId = "";
        //开启事务
        stringRedisTemplate.multi();
        String battleId = stringRedisTemplate.opsForValue().get(WAIT_FOR_KEY);
        //存在对手
        if (!StringUtil.isNullOrEmpty(WAIT_FOR_KEY)) {
            //,battleFlag是为了先跑redis，避免其他业务阻塞redis
            battleFlag = true;
            stringRedisTemplate.delete(WAIT_FOR_KEY);
            //生成对局id
            gameId = IdWorker.get32UUID();
            //写入对局id
            stringRedisTemplate.opsForValue().set(IS_MATCH_KEY + battleId, gameId, 10, TimeUnit.SECONDS);
        } else {
            //不存在，对手自身成为被匹配用户，有效时间10s，需要轮询来刷新以更新自身
            stringRedisTemplate.opsForValue().set(WAIT_FOR_KEY, userId, 10, TimeUnit.SECONDS);
        }
        //事务执行
        stringRedisTemplate.exec();

        if (battleFlag) {
            //发出心跳信息,有效期2min
            stringRedisTemplate.opsForValue().set(ON_GAME_KEY + userId, ON_GAME_VALUE, 2, TimeUnit.MINUTES);
            //有对手，创建对局
            Game game = new Game();
            game.setId(gameId);
            game.setPlayer1Id(userId);
            game.setPlayer1Username(username);
            game.setStartTime(new Date());
            //从数据库取出一道题
            game.setQuestionId("1");
            game.setQuestionTitle("123");
            game.setType("1");
            gameService.save(game);
            return game;
        } else {
            return null;
        }
    }

    @Override
    public Game waitForMatching() {
        //尽量不要用抛异常
        //取出用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();
        String username = authentication.getName();

        boolean matchFlag = false;

        //事务
        stringRedisTemplate.multi();
        String gameId = stringRedisTemplate.opsForValue().get(IS_MATCH_KEY + userId);
        //自己已经被匹配到
        if (gameId != null) {
            matchFlag = true;
        } else {
            //自身没有被匹配到,刷新匹配队列表示我还在匹配状态
            stringRedisTemplate.opsForValue().set(WAIT_FOR_KEY, userId, 10, TimeUnit.SECONDS);
        }
        stringRedisTemplate.exec();

        //补充对战信息
        if (matchFlag) {
            //发出心跳信息,有效期2min
            stringRedisTemplate.opsForValue().set(ON_GAME_KEY + userId, ON_GAME_VALUE, 2, TimeUnit.MINUTES);
            //被匹配的是play2
            LambdaUpdateWrapper<Game> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Game::getPlayer2Id, userId).eq(Game::getPlayer2Username, username);
            gameService.update(wrapper);
            return gameService.getById(gameId);
        } else {
            return null;
        }
    }

    @Override
    public void heartBeat(String battleId) {
        //获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String userId = authUser.getUserId();

        //没有并发问题，不用事务
        //表示自己还在游戏中，有效时间3min
        stringRedisTemplate.opsForValue().set(ON_GAME_KEY + userId, ON_GAME_VALUE, 2, TimeUnit.MINUTES);

        //查询对方是否还在对局中
        String battleValue = stringRedisTemplate.opsForValue().get(ON_GAME_KEY + battleId);

        //对方不在对局中，已经放弃或者离线
        if (StringUtil.isNullOrEmpty(battleValue)) {
            throw new MyException(MyErrorCodeEnum.QUIT_ERROR);
        } else {
            //对方还在对局，且已经完成对局
            if (PASS_GAME_VALUE.equals(battleValue)) {
                throw new MyException(MyErrorCodeEnum.PASS_ERROR);
            }
            //否则表示对方还在做题，直接结束
        }
    }

    @Override
    public void submit(Submission submission) {
    }

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
