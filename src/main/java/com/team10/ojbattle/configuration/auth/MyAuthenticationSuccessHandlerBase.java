package com.team10.ojbattle.configuration.auth;

import com.baomidou.mybatisplus.extension.api.R;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功操作
 * @author CJH
 */
@Component
public class MyAuthenticationSuccessHandlerBase extends BaseJSONAuthentication implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //取得账号信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //
        System.out.println("userDetails = " + userDetails);

        //生成token
        System.out.println("初次登录，token还没有，生成新token。。。。。。");
        //如果token为空，则去创建一个新的token
        String token = jwtTokenUtil.generateToken(userDetails);

        //加载前端菜单
        Map<String,Object> map = new HashMap<>(3);
        map.put("username",userDetails.getUsername());
        map.put("auth",userDetails.getAuthorities());
        map.put("token",token);
        //装入token
        R<Map<String,Object>> data = R.ok(map);
        //输出
        this.WriteJSON(request, response, data);
    }
}
