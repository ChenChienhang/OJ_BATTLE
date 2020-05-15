package com.team10.ojbattle.common.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.team10.ojbattle.common.enums.StatusEnum;

/**
 * @author: 陈健航
 * @description: json反序列转换器
 * @since: 2020/5/15 22:28
 * @version: 1.0
 */
public class StatusEnumConverter implements Converter<Integer, StatusEnum> {

    @Override
    public StatusEnum convert(Integer value) {
        return StatusEnum.valueOf(value);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Integer.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(StatusEnum.class);
    }
}
