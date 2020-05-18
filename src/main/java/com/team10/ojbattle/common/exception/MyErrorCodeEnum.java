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
     * 未登录，请先登录
     */
    HAVEN_NOT_LOGIN_ERROR(10001L, "未登录，请先登录"),

    /**
     * 登陆信息异常，请重新登录
     */
    TOKEN_ERROR(10002L, "会话已过期，请重新登录"),

    /**
     * 权限不足
     */
    ACCESS_ERROR(10003L, "当前用户权限不足"),

    /**
     * 账号错误
     */
    PASSWORD_ERROR(10004L, "密码错误"),

    /**
     * 邮箱已被注册
     */
    EMAIL_REG_ERROR(10005L, "邮箱已被注册"),

    /**
     * 用户不存在
     */
    EMAIL_EXIST_ERROR(10006L, "用户不存在"),

    /**
     * 验证码错误
     */
    VERIFICATION_ERROR(10007L, "验证码错误"),

    /**
     * 昵称已存在
     */
    USERNAME_ERROR(10008L, "昵称已存在"),

    ERROR(10009L, "未知异常"),

    /**
     * 对手长时间未响应，或已经离线或放弃
     */
    QUIT_ERROR(20001L, "对手长时间未响应，或已经离线"),

    /**
     * 对手放弃
     */
    GIVE_UP_ERROR(20002L, "对手已放弃对局"),

    /**
     * 重新进入匹配池
     */
    MATCH_ERROR(20003L, "重新进入匹配池"),

    /**
     * 自己已经被匹配
     */
    CONFIRM_ERROR(20004L, "自己已经被匹配"),

    /**
     * 等待确认匹配
     */
    WAIT_CONFIRM_ERROR(20005L, "等待确认匹配"),

    /**
     * 匹配池数量不足
     */
    KEEP_MATCHING_ERROR(20006L, "匹配池数量不足"),

    /**
     * 对方玩家已经完成并通过验证
     */
    PASS_ERROR(20007L, "对方玩家已经完成并通过验证"),
    ;

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

    public static MyErrorCodeEnum valueOf(long value) {
        for (MyErrorCodeEnum type : MyErrorCodeEnum.values()) {
            if (type.getCode() == value) {
                return type;
            }
        }
        return null;
    }
}
