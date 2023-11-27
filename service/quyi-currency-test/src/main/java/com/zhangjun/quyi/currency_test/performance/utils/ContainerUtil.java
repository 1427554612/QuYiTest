package com.zhangjun.quyi.currency_test.performance.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.currency_test.utils.ParamsEntity;
import com.zhangjun.quyi.utils.JsonUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContainerUtil {

    public static List<ParamsEntity> distinct(List<ParamsEntity> list) throws Exception {
        Set<String> skipSet = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            ParamsEntity entity = (ParamsEntity) list.get(i);
            skipSet.add(JsonUtil.objectMapper.writeValueAsString(entity));
        }
        list.clear();
        skipSet.stream().forEach( str ->{
            try {
                ParamsEntity paramsEntity = JsonUtil.objectMapper.readValue(str, ParamsEntity.class);
                list.add(paramsEntity);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return list;
    }
}
