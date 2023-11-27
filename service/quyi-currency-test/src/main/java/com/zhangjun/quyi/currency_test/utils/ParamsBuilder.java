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

    /**
     * 获取单个字符
     * @param paramsEntities
     * @param key
     * @return
     */
    static Object getStr(List<ParamsEntity> paramsEntities,String key){
        for (ParamsEntity paramsEntity : paramsEntities) {
            if (paramsEntity.getUseName().equals(key) )return paramsEntity.getKeyValue();
        }
        return null;
    }
}
