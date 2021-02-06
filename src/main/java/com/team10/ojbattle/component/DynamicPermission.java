package com.team10.ojbattle.component;


import com.team10.ojbattle.common.exception.MyErrorCodeEnum;
import com.team10.ojbattle.configuration.auth.MyaccessDeniedException;
import com.team10.ojbattle.entity.auth.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @author 陈健航
 */
@Slf4j
@Component
public class DynamicPermission {


    /**
     * 判断有访问API的权限
     *
     * @param httpServletRequest httpServletRequest
     * @param authentication     authentication
     * @return boolean
     * @throws MyaccessDeniedException
     */
    public boolean checkPermission(HttpServletRequest httpServletRequest,
                                   Authentication authentication) throws MyaccessDeniedException {

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthUser) {
            //得到当前的账号
            AuthUser authUser = (AuthUser) principal;
            //当前访问路径
            String requestURI = httpServletRequest.getRequestURI();
            //提交类型
            String urlMethod = httpServletRequest.getMethod();
            log.info("requestURI = " + requestURI);
            //判断当前路径中是否在资源鉴权中
            boolean rs = authUser.getSysBackendApiList().stream().anyMatch(item -> {
                String dbUri = item.getUrl();
                String dbMethod = item.getMethod();
                boolean res = Pattern.compile(dbUri).matcher(requestURI).find();
                //处理null，万一数据库存值
                dbMethod = (dbMethod == null) ? "" : dbMethod;
                //两者都成立，返回真，否则返回假
                return res && urlMethod.equals(dbMethod);
            });
            if (rs) {
                return true;
            } else {
                throw new MyaccessDeniedException(String.valueOf(MyErrorCodeEnum.ACCESS_ERROR.getCode()));
            }
        } else {
            throw new MyaccessDeniedException(String.valueOf(MyErrorCodeEnum.ERROR.getCode()));
        }
    }
}