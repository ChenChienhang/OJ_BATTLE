package com.team10.ojbattle.configuration.auth;

import org.springframework.security.core.AuthenticationException;


/**
 * @author: 陈健航
 * @description: 自定义异常类，继承AuthenticationException
 *   在有throws AuthenticationException方法上捕获
 *   方式：throw new  MyAuthenticationException
 * @since: 2020/4/4 23:21
 * @version: 1.0
 */
public class MyAuthenticationException  extends AuthenticationException {

    public MyAuthenticationException(String msg) {
        super(msg);
    }
}

