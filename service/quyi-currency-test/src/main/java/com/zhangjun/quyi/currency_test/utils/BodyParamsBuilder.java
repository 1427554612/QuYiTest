package com.zhangjun.quyi.currency_test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.utils.JsonUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BodyParamsBuilder implements ParamsBuilder{

    /**
     * 解析源数据从参数列表中替换新的数据
     * @param paramsEntities
     * @param target
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public Object parseParams(List<ParamsEntity> paramsEntities, Object target) throws JsonProcessingException {
        JsonNode jsonNode = null;
        if (target instanceof String){
            jsonNode = JsonUtil.objectMapper.readTree((String) target);
        }
        else jsonNode = JsonUtil.objectMapper.readTree((String) target);
        Iterator<String> stringIterator = jsonNode.fieldNames();
        String resultJson = (String) target;;
        while (stringIterator.hasNext()){
            String key = stringIterator.next();
            String value = jsonNode.get(key).asText().replaceAll("\"","");
            if (value.startsWith("${") || value.endsWith("}")) {
                for (ParamsEntity paramsEntity : paramsEntities) {
                    if (key.equals(paramsEntity.getUseName())){
                        resultJson = updateElStr(resultJson,paramsEntity.getKeyValue(),"${"+key+"}");
                    }
                }
            }
        }
        return resultJson;
    }

    /**
     *
     * @param oldStr：旧字符串
     * @param newStr：新字符串
     * @return
     */
    private static String updateElStr(String oldStr, Object newStr,String key){
        if (!oldStr.contains("${")) return oldStr;
        oldStr = oldStr.replace(key,(String)newStr);
        return oldStr;
    }
}
