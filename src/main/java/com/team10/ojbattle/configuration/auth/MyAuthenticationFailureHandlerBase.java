package com.team10.ojbattle.configuration.auth;


import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author: 陈健航
 * @description: 登录失败操作
 * @since: 2020/4/4 23:21
 * @version: 1.0
 */
@Component
public class MyAuthenticationFailureHandlerBase extends BaseJSONAuthentication implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        R<String> data = R.failed(new IErrorCode() {
            @Override
            public long getCode() {
                return 20005L;
            }

            @Override
            public String getMsg() {
                return "登陆失败" + e.getMessage();
            }
        });
        //输出
        this.WriteJSON(request, response, data);
    }
}