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

    /**
     * 返回值：对局进行中
     */
    public static final String RET_OK = "RET_OK";

    /**
     * 返回值：对方已经退出对局
     */
    public static final String RET_QUIT = "RET_QUIT";

    /**
     * 返回值：对方已经完成对局
     */
    public static final String RET_FINISH = "RET_FINISH";

    /**
     * 心跳保持
     * @param opponentId 对手的id
     * @return RET_QUIT对方退出了游戏或掉线/RET_FINISH对方已完成对局/RET_OK对局进行中
     */
    public String heartBeat(String opponentId) {
        //获取userId
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();

        //没有并发问题，不用事务
        //表示自己还在游戏中，有效时间2min
        stringRedisTemplate.opsForValue().set(ON_GAME_KEY + userId, ON_GAME_VALUE, 2, TimeUnit.MINUTES);

        //查询对方是否还在对局中
        String opponentValue = stringRedisTemplate.opsForValue().get(ON_GAME_KEY + opponentId);

        //对方不在对局中，已经放弃或者离线
        if (opponentValue == null) {
            return RET_QUIT;
        } else {
            //对方还在对局，且已经完成对局
            if (PASS_GAME_VALUE.equals(opponentValue)) {
                return RET_FINISH;
            }
            //否则表示对方还在做题，直接结束
        }
        return RET_OK;
    }

    public void quit() {
        //获取userId
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        //删除自身对局信息，表示已退出
        stringRedisTemplate.delete(ON_GAME_KEY + userId);
    }

    public void finish() {
        //获取userId
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        //把完成标记存入，有效期一天
        stringRedisTemplate.opsForValue().set(ON_GAME_KEY + userId, PASS_GAME_VALUE, 1, TimeUnit.DAYS);
    }

}
