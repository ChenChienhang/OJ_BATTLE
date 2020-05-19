package com.team10.ojbattle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.entity.vo.RegisterVO;
import com.team10.ojbattle.entity.vo.ResetUsrVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     * @param registerVO
     * @return
     */
    boolean register(RegisterVO registerVO);


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
     * @param current
     * @param size
     * @return
     */
    IPage<SysUser> listTopList(Integer current, Integer size);


    /**
     * 发送注册验证码生产者
     *
     * @param email
     */
    void sendFindEmailProcedure(String email);

    /**
     * 重置密码
     *
     * @param resetUsrVO
     */
    void reset(ResetUsrVO resetUsrVO);
}