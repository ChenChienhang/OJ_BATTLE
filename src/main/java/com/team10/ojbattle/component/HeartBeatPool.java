package com.team10.ojbattle.component;

import com.team10.ojbattle.entity.auth.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: 陈健航
 * @description: 心跳池
 * @since: 2020/4/17 15:56
 * @version: 1.0
 */
@Component
public class HeartBeatPool {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 分隔符
     */
    private static final String SEPARATOR = ":";

    /**
     * 表示自己还在对局的key,后面加自己的id
     */
    private static final String ON_GAME_KEY = "BATTLE_ON_GAME";

    /**
     * 还在对局中value
     */
    private static final String ON_GAME_VALUE = "ON_GAME";

    /**
     * 退出游戏value
     */
    private static final String GIVE_UP_VALUE = "GIVE_UP";


    /**
     * 提交value
     */
    private static final String PASS_GAME_VALUE = "PASS_GAME";

    /**
     * 返回值：对局进行中
     */
    public static final String OK_RET = "OK_RET";

    /**
     * 返回值：对方已经放弃对局
     */
    public static final String GIVE_UP_RET = "GIVE_UP_RET";

    /**
     * 返回值：对方掉线
     */
    public static final String QUIT_RET = "QUIT_RET";

    /**
     * 返回值：对方已经完成对局
     */
    public static final String FINISH_RET = "FINISH_RET";

    /**
     * 心跳保持
     *
     * @param gameId
     * @param opponentId
     * @return RET_QUIT对方退出了游戏或掉线/RET_FINISH对方已完成对局/RET_OK对局进行中
     */
    public String keepHeartBeat(String gameId, String opponentId) {
        //获取userId
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();

        //没有并发问题，不用事务
        //表示自己还在游戏中，key=BATTLE_ON_GAME:132564897:49861387,value=ON_GAME,有效时间20s
        stringRedisTemplate.opsForValue().set(ON_GAME_KEY + SEPARATOR + gameId + SEPARATOR + userId, ON_GAME_VALUE, 20, TimeUnit.SECONDS);

        //查询对方是否还在对局中
        String opponentValue = stringRedisTemplate.opsForValue().get(ON_GAME_KEY + SEPARATOR + gameId + SEPARATOR + opponentId);

        String res = OK_RET;
        //对方不在对局中，已经放弃或者离线
        if (opponentValue == null) {
            res = GIVE_UP_RET;
        } else {
            //对方还在对局，且已经完成对局
            if (PASS_GAME_VALUE.equals(opponentValue)) {
                res = FINISH_RET;
            } else if (GIVE_UP_VALUE.equals(opponentValue)) {
                res = GIVE_UP_RET;
            }
            //否则表示对方还在做题，直接结束
        }
        return res;
    }

    public void firstHeartBeat(String gameId) {
        //获取userId
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        stringRedisTemplate.opsForValue().set(ON_GAME_KEY + SEPARATOR + gameId + SEPARATOR + userId, GIVE_UP_VALUE, 20, TimeUnit.SECONDS);
    }

    public void giveUp(String gameId) {
        //获取userId
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        //修改自身对局信息，时间为1天，表示已退出
        stringRedisTemplate.opsForValue().set(ON_GAME_KEY + SEPARATOR + gameId + SEPARATOR + userId, GIVE_UP_RET, 1, TimeUnit.DAYS);
    }

    public void finish(String gameId) {
        //获取userId
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        //把完成标记存入，有效期一天
        stringRedisTemplate.opsForValue().set(ON_GAME_KEY + SEPARATOR + gameId + SEPARATOR + userId, PASS_GAME_VALUE, 1, TimeUnit.DAYS);
    }

}
