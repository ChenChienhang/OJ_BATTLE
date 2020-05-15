package com.team10.ojbattle.common.exception;

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
    VERIFICATION_ERROR(10001L, "验证码错误"),

    /**
     * 邮箱已被注册
     */
    EMAIL_REG_ERROR(10002L, "邮箱已被注册"),

    /**
     * 昵称已存在
     */
    USERNAME_ERROR(10003L, "昵称已存在"),

    /**
     * 密码或账号错误
     */
    LOGIN_ERROR(10004L, "密码错误"),

    /**
     * 登陆信息异常，请重新登录
     */
    TOKEN_ERROR(10005L, "登陆信息异常，请重新登录"),

    /**
     * 用户不存在
     */
    EMAIL_EXIST_ERROR(10006L, "用户不存在"),

    /**
     * 未登录，请先登录
     */
    HAVEN_NOT_LOGIN_ERROR(10007L, "未登录，请先登录"),

    /**
     * 对方玩家已经完成并通过验证
     */
    PASS_ERROR(10008L, "对方玩家已经完成并通过验证"),

    /**
     * 对局信息异常
     */
    GAME_ERROR(10009L, "对局信息异常"),

    /**
     * 重新进入匹配池
     */
    MATCH_ERROR(10010L, "重新进入匹配池"),

    /**
     * 自己已经被匹配
     */
    CONFIRM_ERROR(10011L, "自己已经被匹配"),

    /**
     * 等待确认匹配
     */
    WAIT_CONFIRM_ERROR(10012L, "等待确认匹配"),

    /**
     * 匹配池数量不足
     */
    KEEP_MATCHING_ERROR(10013L, "匹配池数量不足"),

    /**
     * 对手长时间未响应，或已经离线或放弃
     */
    QUIT_ERROR(10014L, "对手长时间未响应，或已经离线或放弃");

    private final long code;

    private final String msg;

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
