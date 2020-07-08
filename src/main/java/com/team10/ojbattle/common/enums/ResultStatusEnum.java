package com.team10.ojbattle.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author: 陈健航
 * @description: oj判题返回状态枚举
 * @since: 2020/5/15 22:24
 * @version: 1.0
 */
public enum ResultStatusEnum implements IEnum<Integer> {

    /**
     * SUCCESS
     */
    WRONG_ANSWER(-1, "解答错误"),

    /**
     * SUCCESS
     */
    SUCCESS(0, "通过"),

    /**
     * CPU_TIME_LIMIT_EXCEEDED
     */
    CPU_TIME_LIMIT_EXCEEDED(1, "超过CPU时间限制"),

    /**
     * REAL_TIME_LIMIT_EXCEEDED
     */
    REAL_TIME_LIMIT_EXCEEDED(2, "超出实时时间"),

    /**
     * 超出内存限制
     */
    MEMORY_LIMIT_EXCEEDED(3, "超出内存限制"),

    /**
     * 运行时错误
     */
    RUNTIME_ERROR(4, "运行时错误"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR(5, "系统错误"),
    /**
     * 系统错误
     */
    COMPILER_ERROR(6, "系统错误");

    ResultStatusEnum(Integer value, String detail) {
        this.value = value;
        this.detail = detail;
    }

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String detail;

    public static ResultStatusEnum valueOf(int code) {
        for (ResultStatusEnum type : ResultStatusEnum.values()) {
            if (type.getValue() == code) {
                return type;
            }
        }
        return null;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getDetail() {
        return detail;
    }
}
