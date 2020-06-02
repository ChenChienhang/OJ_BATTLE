package com.team10.ojbattle.configuration.auth;


import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.common.exception.MyErrorCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


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
                                        AuthenticationException e) throws IOException {

        R<String> data = R.failed(Objects.requireNonNull(MyErrorCodeEnum.valueOf(Long.parseLong(e.getMessage()))));
        //输出
        this.WriteJSON(request, response, data);
    }
}