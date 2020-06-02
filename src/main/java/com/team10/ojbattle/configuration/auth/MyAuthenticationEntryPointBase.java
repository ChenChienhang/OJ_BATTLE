package com.team10.ojbattle.configuration.auth;

import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.common.exception.MyErrorCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 陈健航
 * @description: 身份校验失败处理器，如 token 错误
 * @since: 2020/4/4 23:21
 * @version: 1.0
 */
@Component
public class MyAuthenticationEntryPointBase extends BaseJSONAuthentication implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        R<String> data = R.failed(MyErrorCodeEnum.TOKEN_ERROR);
        //输出
        this.WriteJSON(request, response, data);
    }
}
