package com.team10.ojbattle.exception;

import com.baomidou.mybatisplus.extension.api.IErrorCode;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/3/22 12:07
 * @version: 1.0
 */
public enum MyErrorCodeEnum implements IErrorCode {

    /**
     * 验证码错误
     */
    VERIFICATION_ERROR(20001L, "验证码错误"),

    /**
     * 邮箱已被注册
     */
    EMAIL_REG_ERROR(20002L, "邮箱已被注册"),

    /**
     * 昵称已存在
     */
    USERNAME_ERROR(20003L, "昵称已存在"),

    /**
     * 密码或账号错误
     */
    LOGIN_ERROR(20004L, "密码错误"),

    /**
     * 登陆信息异常，请重新登录
     */
    TOKEN_ERROR(20005L, "登陆信息异常，请重新登录"),

    /**
     * 用户不存在
     */
    EMAIL_EXIST_ERROR(20006L,"用户不存在"),

    /**
     *
     */
    HAVEN_NOT_LOGIN_ERROR(20007L,"未登录，请先登录"),

    /**
     * 正在匹配对手中
     */
    KEEP_MATCHING_ERROR(20011L, "正在匹配对手中"),

    /**
     * 对手长时间未响应，或已经离线或放弃
     */
    QUIT_ERROR(20012L, "对手长时间未响应，或已经离线或放弃"),

    /**
     * 对方玩家已经完成并通过验证
     */
    PASS_ERROR(20013L, "对方玩家已经完成并通过验证"),


    /**
     * 对局信息异常
     */
    GAME_ERROR(20014L, "对局信息异常");

    private long code;

    private String msg;

    MyErrorCodeEnum(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public long getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
