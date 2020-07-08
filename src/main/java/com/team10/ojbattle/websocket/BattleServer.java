package com.team10.ojbattle.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.common.utils.ThreadPool;
import com.team10.ojbattle.component.JwtTokenUtil;
import com.team10.ojbattle.entity.DelayTask;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.service.BattleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * WebSocket服务端示例
 *
 * @author wallimn，http://wallimn.iteye.com
 */
@Slf4j
@ServerEndpoint(value = "/battle/on")
@Component
public class BattleServer {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
     */
    private static final ConcurrentHashMap<String, Session> SESSION_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, AuthUser> AUTHERUSER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    private static final DelayQueue<DelayTask> ENTER_CHECK_QUEUE = new DelayQueue<>();
    private static final DelayQueue<DelayTask> DELETE_CHECK_QUEUE = new DelayQueue<>();
    private static UserDetailsService userDetailsService;
    private static JwtTokenUtil jwtTokenUtil;
    private static StringRedisTemplate stringRedisTemplate;
    private static BattleService battleService;
    private String gameId;

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendMessageById(String sessionId, String message) {
        try {
            SESSION_CONCURRENT_HASH_MAP.get(sessionId).getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Autowired
    public void setBattleService(BattleService battleService) {
        BattleServer.battleService = battleService;
    }

    @Autowired
    @Qualifier("sysUserService")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        BattleServer.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        BattleServer.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        BattleServer.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("websocket已连接");
        SESSION_CONCURRENT_HASH_MAP.put(session.getId(), session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //退出对局
        quitGame(session);
        //删除匹配池中的
        SESSION_CONCURRENT_HASH_MAP.remove(session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("message: {}", message);
        JSONObject jsonObject = JSON.parseObject(message);
        String code = jsonObject.getString("code");
        switch (code) {
            case "0": {
                //返回心跳信息
                heartbeat(session);
                break;
            }
            case "1": {
                init(jsonObject, session);
                break;
            }
            case "2": {
                judge(jsonObject, session);
                break;
            }
            case "3": {
                //退出游戏
                log.info("有人发起退出游戏");
                quitGame(session);
                break;
            }
            default:
        }
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        SESSION_CONCURRENT_HASH_MAP.remove(session.getId());
        log.error("error：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 用户提交代码，服务端判断
     *
     * @param data
     * @param session
     */
    private void judge(JSONObject data, Session session) {
        //获取代码
        String src = data.getString("src");
        //编程语言
        Integer language = Integer.parseInt(data.getString("language"));
        //判题
        JSONObject ret = battleService.judge(AUTHERUSER_CONCURRENT_HASH_MAP.get(session.getId()), src, language, Long.parseLong(gameId));
        //推送给对方
        if ((Integer) ret.getJSONObject("data").get("result") == 0) {
            Cursor<String> cursor = stringRedisTemplate.opsForSet().scan(gameId, ScanOptions.NONE);
            while (cursor.hasNext()) {
                String sessionId = cursor.next();
                if (!sessionId.equals(session.getId())) {
                    JSONObject res = new JSONObject();
                    res.put("code", 3);
                    res.put("msg", "对方已经提交代码并通过");
                    battleService.cLoseGame(AUTHERUSER_CONCURRENT_HASH_MAP.get(sessionId), Long.parseLong(gameId), false);
                    sendMessageById(sessionId, res.toJSONString());
                }
            }
        }
        sendMessageById(session.getId(), ret.toJSONString());
    }

    /**
     * 客户端通过发送token初始化websocket信息
     *
     * @param data
     * @param session
     */
    private void init(JSONObject data, Session session) {
        String token = data.getString("token");
        String gameId = data.getString("gameId");
        this.gameId = gameId;
        log.info("session id add {}", session.getId());
        //验证用户
        AuthUser authUser = verify(token);
        //把sessionId放进去
        JSONObject res = new JSONObject();
        //返回前端
        if (authUser == null) {
            res.put("code", -1);
            res.put("msg", "登录信息异常，请重新登录");
        } else {
            res.put("code", 0);
            //使用redis建立状态信息。
            stringRedisTemplate.opsForSet().add(gameId, session.getId());
            //存入AutherUser
            AUTHERUSER_CONCURRENT_HASH_MAP.put(session.getId(), authUser);
            //延时检查
            JSONObject delayJSONObject = new JSONObject();
            delayJSONObject.put("gameId", gameId);
            delayJSONObject.put("sessionId", session.getId());
            ENTER_CHECK_QUEUE.offer(new DelayTask(delayJSONObject.toJSONString(), 3000));
            startEnterDelayCheck();
        }
        sendMessage(session, res.toJSONString());
    }

    /**
     * 在redis删除对局信息，延时检查，防止刷新浏览器造成的假退出
     *
     * @param session
     */
    private void quitGame(Session session) {
        log.info("进入退出游戏方法");
        log.info("session id 准备被移除 {}", session.getId());
        AUTHERUSER_CONCURRENT_HASH_MAP.remove(session.getId());
        stringRedisTemplate.opsForSet().remove(this.gameId, session.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gameId", this.gameId);
        log.info("添加退出游戏延时队列");
        DELETE_CHECK_QUEUE.offer(new DelayTask(jsonObject.toJSONString(), 10000));
        startDeleteDelayCheck();
    }

    /**
     * 退出游戏延时检查
     */
    private void startDeleteDelayCheck() {
        ThreadPool.getInstance().execute(() -> {
            while (true) {
                try {
                    DelayTask delayTask = DELETE_CHECK_QUEUE.take();
                    log.info("从延时队列取出");
                    JSONObject subject = JSON.parseObject(delayTask.getSubject());
                    String gameId = subject.getString("gameId");
                    Long size = stringRedisTemplate.opsForSet().size(gameId);
                    log.info("退出延时队列大小{}", size);
                    if (size != null && size == 1 && !battleService.checkGameIsOver(gameId)) {
                        Cursor<String> cursor = stringRedisTemplate.opsForSet().scan(gameId, ScanOptions.NONE);
                        while (cursor.hasNext()) {
                            JSONObject res = new JSONObject();
                            res.put("code", 3);
                            res.put("msg", "对方已放弃对局");
                            log.info("发送退出延时消息");
                            String sessionId = cursor.next();
                            battleService.cLoseGame(AUTHERUSER_CONCURRENT_HASH_MAP.get(sessionId), Long.parseLong(gameId), false);
                            sendMessageById(sessionId, res.toJSONString());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void heartbeat(Session session) {
        JSONObject res = new JSONObject();
        res.put("code", 0);
        sendMessage(session, res.toJSONString());
    }


    /**
     * 启动延时检查加入
     */
    private void startEnterDelayCheck() {
        ThreadPool.getInstance().execute(() -> {
            while (true) {
                try {
                    DelayTask delayTask = ENTER_CHECK_QUEUE.take();
                    JSONObject jsonObject = JSON.parseObject(delayTask.getSubject());
                    String sessionId = jsonObject.getString("sessionId");
                    String gameId = jsonObject.getString("gameId");
                    Long size = stringRedisTemplate.opsForSet().size(gameId);
                    if (size != null && size < 2) {
                        JSONObject res = new JSONObject();
                        res.put("code", 3);
                        res.put("msg", "对方已放弃对局");
                        sendMessageById(sessionId, res.toJSONString());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 验证权限，因为websocket好像没有带请求头，就下放到这里进行
     */
    private AuthUser verify(String message) {
        String token = message.replace("Bearer", "").trim();
        AuthUser res = null;
        boolean tokenExpired;
        try {
            tokenExpired = jwtTokenUtil.isTokenExpired(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //token未过期
        if (!tokenExpired) {
            //通过令牌获取邮箱
            String email = jwtTokenUtil.getSubjectFromToken(token);
            log.info("email" + email);
            //验证令牌有效性,通过用户信息得到UserDetails,AuthUser是UserDetails的子类
            res = (AuthUser) userDetailsService.loadUserByUsername(email);
        }
        return res;
    }


}

