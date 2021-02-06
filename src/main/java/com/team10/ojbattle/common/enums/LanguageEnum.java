package com.team10.ojbattle.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author: 陈健航
 * @description: 编写语言枚举
 * @since: 2020/5/15 21:53
 * @version: 1.0
 */
public enum LanguageEnum implements IEnum<Integer> {

    /**
     * c
     */
    C(0, "C"),

    /**
     * cpp
     */
    CPP(1, "C++"),

    /**
     * java
     */
    JAVA(2, "Java"),

    /**
     * python3
     */
    PYTHON3(3, "Python"),

    /**
     * python3
     */
    PYTHON2(4, "Python");

    LanguageEnum(Integer value, String detail) {
        this.value = value;
        this.detail = detail;
    }

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String detail;

    public static LanguageEnum valueOf(int code) {
        for (LanguageEnum type : LanguageEnum.values()) {
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
