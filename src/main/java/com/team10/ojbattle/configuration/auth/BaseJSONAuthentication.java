package com.team10.ojbattle.configuration.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team10.ojbattle.component.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: 陈健航
 * @description: 封装输出JSON格式的类
 * @since: 2020/4/4 23:21
 * @version: 1.0
 */
public abstract class BaseJSONAuthentication {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * 输出JSON
     *
     * @param request
     * @param response
     * @param data
     * @throws IOException
     * @throws ServletException
     */
    protected void WriteJSON(HttpServletRequest request,
                             HttpServletResponse response,
                             Object data) throws IOException {
        //这里很重要，否则页面获取不到正常的JSON数据集
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Method", "POST,GET");
        //输出JSON
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(data));
        out.flush();
        out.close();
    }
}
