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

    TypeEnum(Integer value, String detail) {
        this.value = value;
        this.detail = detail;
    }

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String detail;

    public static TypeEnum valueOf(int code) {
        for (TypeEnum type : TypeEnum.values()) {
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
