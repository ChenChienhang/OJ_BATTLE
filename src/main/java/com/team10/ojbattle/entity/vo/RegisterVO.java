package com.team10.ojbattle.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/19 11:36
 * @version: 1.0
 */
@Data
@NoArgsConstructor
public class RegisterVO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String verificationCode;
}
