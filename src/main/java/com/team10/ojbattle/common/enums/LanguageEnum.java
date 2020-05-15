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
     * cpp
     */
    CPP(1, "C++"),

    /**
     * java
     */
    JAVA(2,"Java"),

    /**
     * python
     */
    PYTHON(3, "Python");

    LanguageEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String name;

    public static LanguageEnum valueOf(int value) {
        for (LanguageEnum type : LanguageEnum.values()){
            if (type.getValue() == value){
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
