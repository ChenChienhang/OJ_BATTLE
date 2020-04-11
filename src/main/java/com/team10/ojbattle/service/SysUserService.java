package com.team10.ojbattle.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.team10.ojbattle.entity.SysUser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * (User)表服务接口
 *
 * @author 陈健航
 * @since 2020-04-04 23:43:17
 */
public interface SysUserService extends IService<SysUser> {


    /**
     * 个性化登录验证
     * @param username
     * @param rawPassword
     * @return
     */
    boolean checkLogin(String username, String rawPassword);

    /**
     * 注册
     * @param user
     * @return
     * @throws Exception
     */
    boolean register(Map<String ,String> user);


    /**
     * 发送验证码生产者
     * @param email
     */
    void sendRegEmailProcedure(String email);

    /**
     * 发送验证码消费者
     * @param map
     */
    void sendRegEmailConsumer(Map<String, String> map);

}