package com.team10.ojbattle.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.common.utils.ThreadPool;
import com.team10.ojbattle.entity.DelayTask;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.service.BattleService;
import com.team10.ojbattle.websocket.MatchingServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: 陈健航
 * @description: 匹配池
 * @since: 2020/4/17 12:11
 * @version: 1.0
 */
@Slf4j
@Component
public class MatchingPool {

    /**
     * 延时队列
     */
    private final DelayQueue<DelayTask> delayQueue;

    public static boolean isRun = false;

    private final StringRedisTemplate stringRedisTemplate;

    private final BattleService battleService;

    private final Lock lock;

    @Autowired
    public MatchingPool(BattleService battleService, StringRedisTemplate stringRedisTemplate) {
        this.delayQueue = new DelayQueue<>();
        this.battleService = battleService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.lock = new ReentrantLock();
        stringRedisTemplate.opsForZSet().removeRange(MATCH_POOL, 0, stringRedisTemplate.opsForZSet().size(MATCH_POOL) - 1);
    }

    /**
     * 匹配池的key
     */
    private static final String MATCH_POOL = "MATCH_POOL";


    /**
     * 加入线程池
     *
     * @param authUser
     */
    public void addPool(AuthUser authUser) {
        lock.lock();
        try {
            stringRedisTemplate.opsForZSet().add(MATCH_POOL, JSON.toJSONString(authUser), authUser.getRating());
        } finally {
            lock.unlock();
        }

        delayQueue.offer(new DelayTask(JSON.toJSONString(authUser), 10000));
        startMatch();
        log.info("add  the pool {}", stringRedisTemplate.opsForZSet().size(MATCH_POOL));
    }

    /**
     * 启动线程进行匹配
     */
    private void startMatch() {
        //启动线程
        if (!isRun) {
            ThreadPool.getInstance().execute(() -> {
                log.info("start match");
                isRun = true;
                while (true) {
                    //上锁
                    log.info("start while");
                    try {
                        DelayTask delayTask = delayQueue.take();
                        lock.lock();
                        try {
                            Long rank = stringRedisTemplate.opsForZSet().rank(MATCH_POOL, delayTask.getSubject());
                            //自身还在池子里
                            if (rank != null && rank >= 0) {
                                Long size = stringRedisTemplate.opsForZSet().size(MATCH_POOL);
                                //池子数量还足够
                                if (size != null && size > 1) {
                                    Set<String> range;
                                    //第一个,获取后面的，否则获取前面的元素
                                    if (rank == 0) {
                                        range = stringRedisTemplate.opsForZSet().range(MATCH_POOL, rank, rank + 1);
                                        stringRedisTemplate.opsForZSet().removeRange(MATCH_POOL, rank, rank + 1);
                                    } else {
                                        range = stringRedisTemplate.opsForZSet().range(MATCH_POOL, rank - 1, rank);
                                        stringRedisTemplate.opsForZSet().removeRange(MATCH_POOL, rank - 1, rank);
                                    }
                                    List<AuthUser> authUsers = new ArrayList<>();
                                    if (range != null) {
                                        for (String string : range) {
                                            authUsers.add(JSON.parseObject(string, AuthUser.class));
                                        }
                                    }
                                    Long gameId = battleService.openGame(authUsers.get(0), authUsers.get(1));
                                    //发送通知
                                    JSONObject res = new JSONObject();
                                    res.put("code", "1");
                                    res.put("gameId", String.valueOf(gameId));
                                    log.info("send the match info to the session");
                                    try {
                                        MatchingServer.sendMessageById(authUsers.get(0).getSubject(), res.toJSONString());
                                        MatchingServer.sendMessageById(authUsers.get(1).getSubject(), res.toJSONString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    //池子数量不足，重新加入,等待10s
                                    delayQueue.offer(new DelayTask(delayTask.getSubject(), 10000));
                                    log.info("the pool is not big enough {}", size);
                                }
                            }
                        } finally {
                            //释放锁
                            lock.unlock();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 从匹配池里删除
     */
    public void remove(AuthUser authUser) {
        lock.lock();
        try {
            Long remove = stringRedisTemplate.opsForZSet().remove(MATCH_POOL, JSON.toJSONString(authUser));
            log.info("remove fom matching pool:" + remove);
        } catch (Exception e) {
            log.info(authUser + "： 已不存在匹配池");
        } finally {
            lock.unlock();
        }

    }


}
