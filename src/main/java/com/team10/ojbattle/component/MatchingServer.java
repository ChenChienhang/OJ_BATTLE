package com.team10.ojbattle.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.entity.auth.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 匹配WebSocket服务端
 *
 * @author wallimn，http://wallimn.iteye.com
 */
@Slf4j
@ServerEndpoint(value = "/battle/match")
@Component
public class MatchingServer {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
     */
    private static final ConcurrentHashMap<String, Session> SESSION_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, AuthUser> AUTHER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    private static UserDetailsService userDetailsService;

    private static JwtTokenUtil jwtTokenUtil;

    private static MatchingPool matchingPool;

    @Autowired
    @Qualifier("sysUserService")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        MatchingServer.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        MatchingServer.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setMatchingPool(MatchingPool matchingPool) {
        MatchingServer.matchingPool = matchingPool;
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
        log.info("somebody has close the session");
        SESSION_CONCURRENT_HASH_MAP.remove(session.getId());
        //删除匹配池中的
        matchingPool.remove(AUTHER_CONCURRENT_HASH_MAP.get(session.getId()));
        AUTHER_CONCURRENT_HASH_MAP.remove(session.getId());

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
                HashMap<String, String> res = new HashMap<>(1);
                res.put("code", "0");
                sendMessage(session, JSON.toJSONString(res));
                break;
            }
            case "1": {
                String token = jsonObject.getString("token");
                AuthUser authUser = verify(token);
                //把sessionId放进去
                JSONObject res = new JSONObject();
                if (authUser == null) {
                    res.put("code", "-1");
                    res.put("msg", "登录信息异常，请重新登录");
                } else {
                    authUser.setSubject(session.getId());
                    AUTHER_CONCURRENT_HASH_MAP.put(session.getId(), authUser);
                    matchingPool.addPool(authUser);
                    res.put("code", "0");
                }
                sendMessage(session, res.toJSONString());
            }
            default:
        }
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
        matchingPool.remove(AUTHER_CONCURRENT_HASH_MAP.get(session.getId()));
        AUTHER_CONCURRENT_HASH_MAP.remove(session.getId());
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

