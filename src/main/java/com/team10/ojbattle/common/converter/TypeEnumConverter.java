package com.team10.ojbattle.common.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.team10.ojbattle.common.enums.TypeEnum;


/**
 * @author: 陈健航
 * @description: json反序列转换器
 * @since: 2020/5/15 21:57
 * @version: 1.0
 */
public class TypeEnumConverter implements Converter<Integer, TypeEnum> {

    @Override
    public TypeEnum convert(Integer value) {
        return TypeEnum.valueOf(value);
    }

    /**
     * 输入值类型
     * @param typeFactory
     * @return
     */
    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Integer.class);
    }

    /**
     * 转换器输出值类型
     * @param typeFactory
     * @return
     */
    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(TypeEnum.class);
    }
}