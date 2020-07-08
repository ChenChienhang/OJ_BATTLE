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
public enum ProblemDifficultyEnum implements IEnum<Integer> {
    /**
     * cpp
     */
    EASY(0, "简单"),

    /**
     * java
     */
    MEDIUM(1, "中等"),

    /**
     * python
     */
    HARD(2, "困难");

    ProblemDifficultyEnum(Integer value, String detail) {
        this.value = value;
        this.detail = detail;
    }

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String detail;

    public static ProblemDifficultyEnum valueOf(int code) {
        for (ProblemDifficultyEnum type : ProblemDifficultyEnum.values()) {
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
