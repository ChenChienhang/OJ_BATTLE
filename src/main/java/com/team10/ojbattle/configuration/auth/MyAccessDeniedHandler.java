package com.team10.ojbattle.configuration.auth;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author: 陈健航
 * @description: 权限校验处理器
 * @since: 2020/4/4 23:21
 * @version: 1.0
 */
@Component
public class MyAccessDeniedHandler extends BaseJSONAuthentication implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //装入token
        R<String> data = R.failed("权限不足:"+accessDeniedException.getMessage());
        //输出
        this.WriteJSON(request, response, data);
    }
}
