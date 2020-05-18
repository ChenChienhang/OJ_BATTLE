package com.team10.ojbattle.configuration.auth;

import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.common.exception.MyErrorCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


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
                       AccessDeniedException accessDeniedException) throws IOException {

        //装入token
        R<String> data = R.failed(Objects.requireNonNull(MyErrorCodeEnum.valueOf(Long.parseLong(accessDeniedException.getMessage()))));
        //输出
        this.WriteJSON(request, response, data);
    }
}
