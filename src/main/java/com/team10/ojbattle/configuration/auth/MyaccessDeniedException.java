package com.team10.ojbattle.configuration.auth;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/4/4 23:21
 * @version: 1.0
 */
public class MyaccessDeniedException extends AccessDeniedException {


    public MyaccessDeniedException(String msg) {
        super(msg);
    }
}
