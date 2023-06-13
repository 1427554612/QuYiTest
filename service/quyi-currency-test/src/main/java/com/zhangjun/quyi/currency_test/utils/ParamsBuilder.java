package com.zhangjun.quyi.currency_test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

@FunctionalInterface
public interface ParamsBuilder {

    /**
     * 构建方法
     * @return
     */
    Object parseParams(List<ParamsEntity> paramsEntities, Object target) throws JsonProcessingException;
}
