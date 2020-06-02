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
public enum StatusEnum implements IEnum<Integer> {

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
     * 系统错误，编译错误？
     */
    SYSTEM_ERROR(5, "系统错误");

    StatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String name;

    public static StatusEnum valueOf(int value) {
        for (StatusEnum type : StatusEnum.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
