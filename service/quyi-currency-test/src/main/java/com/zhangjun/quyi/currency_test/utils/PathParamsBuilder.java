package com.zhangjun.quyi.currency_test.utils;

import java.util.List;

public class PathParamsBuilder implements ParamsBuilder{


    @Override
    public Object parseParams(List<ParamsEntity> paramsEntities, Object target) {
        String targetStr = (String)target;
        targetStr = targetStr.replaceAll("\\$","").replaceAll("\\{","").replaceAll("}","");
        for (ParamsEntity paramsEntity : paramsEntities) {
            if (targetStr.equals(paramsEntity.getUseName())) targetStr = (String) paramsEntity.getKeyValue();
        }
        return targetStr;
    }
}
