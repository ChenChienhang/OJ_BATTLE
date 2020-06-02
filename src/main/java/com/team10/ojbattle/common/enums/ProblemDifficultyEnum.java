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

    ProblemDifficultyEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String name;

    public static ProblemDifficultyEnum valueOf(int value) {
        for (ProblemDifficultyEnum type : ProblemDifficultyEnum.values()){
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
