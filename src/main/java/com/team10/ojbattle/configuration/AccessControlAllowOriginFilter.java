package com.team10.ojbattle.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/17 20:27
 * @version: 1.0
 */
@Slf4j
@Component
public class AccessControlAllowOriginFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) {
        log.info("init the AccessControlAllowOriginFilter");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods",
                "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization");
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {
        log.info("destroy the AccessControlAllowOriginFilter");
    }
}
