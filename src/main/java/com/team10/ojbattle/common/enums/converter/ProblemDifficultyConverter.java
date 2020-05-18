package com.team10.ojbattle.common.enums.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.team10.ojbattle.common.enums.LanguageEnum;
import com.team10.ojbattle.common.enums.ProblemDifficultyEnum;

/**
 * @author: 陈健航
 * @description: json反序列转换器
 * @since: 2020/5/15 21:57
 * @version: 1.0
 */
public class ProblemDifficultyConverter implements Converter<Integer, ProblemDifficultyEnum> {

    @Override
    public ProblemDifficultyEnum convert(Integer value) {
        return ProblemDifficultyEnum.valueOf(value);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Integer.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(ProblemDifficultyEnum.class);
    }
}
