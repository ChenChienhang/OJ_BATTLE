package com.team10.ojbattle.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/15 18:41
 * @version: 1.0
 */
public enum TypeEnum implements IEnum<Integer> {

    /**
     * 对局模式
     */
    Battle(0, "对局模式"),

    /**
     * 练习模式
     */
    Practice(1, "练习模式");

    TypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String name;

    public static TypeEnum valueOf(int value) {
        for (TypeEnum type : TypeEnum.values()){
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
