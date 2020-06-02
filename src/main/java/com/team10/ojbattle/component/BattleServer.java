package com.team10.ojbattle.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.common.utils.ThreadPool;
import com.team10.ojbattle.entity.DelayTask;
import com.team10.ojbattle.entity.auth.AuthUser;
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

    private static final ConcurrentHashMap<String, String> GAMEID_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    private static UserDetailsService userDetailsService;

    private static JwtTokenUtil jwtTokenUtil;

    private static StringRedisTemplate stringRedisTemplate;

    private static final DelayQueue<DelayTask> ENTER_CHECK_QUEUE = new DelayQueue<>();

    private static final DelayQueue<DelayTask> DELETE_CHECK_QUEUE = new DelayQueue<>();

    private static boolean isEnterCheckRun = false;

    private static boolean isDeleteCheckRun = false;


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
        log.info("!!!websocket connected");
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
        GAMEID_CONCURRENT_HASH_MAP.remove(session.getId());

    }

    /**
     * 在redis删除对局信息，延时检查，防止刷新浏览器造成的假退出
     *
     * @param session
     */
    private void quitGame(Session session) {
        stringRedisTemplate.opsForSet().remove(GAMEID_CONCURRENT_HASH_MAP.get(session.getId()), session.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gameId", GAMEID_CONCURRENT_HASH_MAP.get(session.getId()));
        DELETE_CHECK_QUEUE.offer(new DelayTask(jsonObject.toJSONString(), 10000));
        startDeleteDelayCheck();
    }

    private void startDeleteDelayCheck() {
        ThreadPool.getInstance().execute(() -> {
            isDeleteCheckRun = true;
            while (isDeleteCheckRun) {
                try {
                    DelayTask delayTask = DELETE_CHECK_QUEUE.take();
                    JSONObject subject = JSON.parseObject(delayTask.getSubject());
                    String gameId = subject.getString("gameId");
                    Long size = stringRedisTemplate.opsForSet().size(gameId);
                    if (size != null && size == 1) {
                        Cursor<String> cursor = stringRedisTemplate.opsForSet().scan(gameId, ScanOptions.NONE);
                        while (cursor.hasNext()) {
                            JSONObject res = new JSONObject();
                            res.put("code", 3);
                            res.put("msg", "对方已放弃对局");
                            sendMessageById(cursor.next(), res.toJSONString());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (DELETE_CHECK_QUEUE.isEmpty()) {
                        isDeleteCheckRun = false;
                    }
                }

            }
        });
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
                JSONObject res = new JSONObject();
                res.put("code", 0);
                sendMessage(session, res.toJSONString());
                break;
            }
            case "1": {
                String token = jsonObject.getString("token");
                String gameId = jsonObject.getString("gameId");
                GAMEID_CONCURRENT_HASH_MAP.put(session.getId(), gameId);
                //验证用户
                AuthUser authUser = verify(token);
                //把sessionId放进去
                JSONObject res = new JSONObject();
                //返回前端
                if (authUser == null) {
                    res.put("code", -1);
                    res.put("msg", "登录信息异常，请重新登录");
                } else {
                    authUser.setSubject(session.getId());
                    res.put("code", 0);
                    //使用redis建立状态信息。
                    stringRedisTemplate.opsForSet().add(gameId, session.getId());
                    //延时检查
                    JSONObject delayJSONObject = new JSONObject();
                    delayJSONObject.put("gameId", gameId);
                    delayJSONObject.put("sessionId", session.getId());
                    ENTER_CHECK_QUEUE.offer(new DelayTask(delayJSONObject.toJSONString(), 3000));
                    startEnterDelayCheck();
                }
                sendMessage(session, res.toJSONString());
                break;
            }
            case "2": {
                String src = jsonObject.getString("src");
                String language = jsonObject.getString("language");
                String gameId = jsonObject.getString("gameId");
                boolean pass = true;
                //返回判题信息
                if (pass) {
                    JSONObject res = new JSONObject();
                    res.put("code", 2);
                    res.put("status", "status");
                    res.put("flag", "0");
                    sendMessageById(session.getId(), res.toJSONString());
                }
                break;
            }
            case "3": {
                //退出游戏
                quitGame(session);
                break;
            }
            default:
        }
    }

    /**
     * 启动延时检查加入
     */
    private void startEnterDelayCheck() {
        ThreadPool.getInstance().execute(() -> {
            isEnterCheckRun = true;
            while (isEnterCheckRun) {
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
                    if (ENTER_CHECK_QUEUE.isEmpty()) {
                        isEnterCheckRun = false;
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


    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        SESSION_CONCURRENT_HASH_MAP.remove(session.getId());
        GAMEID_CONCURRENT_HASH_MAP.remove(session.getId());
        log.error("error：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

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
}

