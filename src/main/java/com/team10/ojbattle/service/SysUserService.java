package com.team10.ojbattle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.team10.ojbattle.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
     *
     * @param username
     * @param rawPassword
     * @return
     */
    boolean checkLogin(String username, String rawPassword) throws Exception;

    /**
     * 注册
     *
     * @param user
     * @return
     * @throws Exception
     */
    boolean register(Map<String, String> user);


    /**
     * 发送注册验证码生产者
     *
     * @param email
     */
    void sendRegEmailProcedure(String email);

    /**
     * 验证码消费者
     *
     * @param map
     * @throws IOException
     */
    void sendRegEmailConsumer(Map<String, String> map) throws IOException;


    /**
     * 上传头像
     *
     * @param file
     * @return
     * @throws Exception
     */
    String uploadAvatar(MultipartFile file) throws Exception;

    /**
     * 获取ranking前十的用户，只包含部分信息，密码这些信息不会查出来
     *
     * @return
     */
    List<SysUser> listTopList();


    /**
     * 发送注册验证码生产者
     *
     * @param email
     */
    void sendFindEmailProcedure(String email);

    /**
     * 重置密码
     *
     * @param map
     */
    void reset(Map<String, String> map);
}