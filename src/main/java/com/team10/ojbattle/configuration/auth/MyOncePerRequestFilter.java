package com.team10.ojbattle.configuration.auth;


import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.exception.MyErrorCodeEnum;
import com.team10.ojbattle.exception.MyException;
import com.team10.ojbattle.utils.JwtTokenUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 陈健航
 * @description: 拦截器
 * @since: 2020/4/4 23:21
 * @version: 1.0
 */
@Component
public class MyOncePerRequestFilter extends OncePerRequestFilter {

    @Qualifier("sysUserService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        //取出token的请求头
        String header = "Authorization";
        String headerToken = request.getHeader(header);

        System.out.println("headerToken = " + headerToken);
        System.out.println("request getMethod = " + request.getMethod());

        if (!StringUtil.isNullOrEmpty(headerToken)) {
            //要去掉约束前缀。
            String token = headerToken.replace("Bearer", "").trim();
            System.out.println("token = " + token);

            //判断令牌是否过期，默认是一周，若过期，抛出异常，强制登录，生成新token。
            boolean tokenExpired;
            try {
                tokenExpired = this.jwtTokenUtil.isTokenExpired(token);
            } catch (Exception e) {
                throw new MyException(MyErrorCodeEnum.TOKEN_ERROR);
            }

            //token未过期
            if (!tokenExpired) {
                //通过令牌获取用户名称
                String username = jwtTokenUtil.getUsernameFromToken(token);
                System.out.println("username = " + username);

                //判断用户不为空，且SecurityContextHolder授权信息还是空的
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    //通过用户信息得到UserDetails,AuthUser是UserDetails的子类
                    AuthUser authUser = (AuthUser)userDetailsService.loadUserByUsername(username);

                    //验证令牌有效性
                    //不存在该用户，抛出错误,因为假token比较少出现，所以可以直接赋予角色
                    if (authUser == null) {
                        throw new MyException(MyErrorCodeEnum.TOKEN_ERROR);
                    }

                    // 将用户信息存入 authentication，方便后续校验
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    authUser,
                                    null,
                                    authUser.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        //放行
        chain.doFilter(request, response);
    }
}
