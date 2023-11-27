package com.zhangjun.quyi.currency_test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamsSetUtil {

    /**
     * 从响应中设置参数
     * @param responseBody
     * @return
     */
    public static List<ParamsEntity> setParamsByResponse(String responseBody, Map<String,String> keyValueMap) throws JsonProcessingException {
        List<ParamsEntity> list = new ArrayList<>();
        for (Map.Entry<String, String> stringObjectEntry : keyValueMap.entrySet()) {
            String key = stringObjectEntry.getKey();
            String value = (String)stringObjectEntry.getValue();
            JsonNode jsonNode = JsonUtil.objectMapper.readTree(responseBody);
            if (value.contains(".")){
                String[] split = value.split("\\.");
                for (String s : split) {
                    jsonNode = jsonNode.get(s);
                }
            }
            if (null == jsonNode.asText() || "".equals(jsonNode.asText())) continue;
            list.add(new ParamsEntity(key,"responseBody",value,jsonNode.asText()));
        }
        return list;
    }


    /**
     * 从请求中设置
     * @param requestBody
     * @param keyValueMap
     * @return
     */
    public static List<ParamsEntity> setParamsByRequest(String requestBody,Map<String,String> keyValueMap) throws JsonProcessingException {
        Map<String,Object> map = JsonUtil.objectMapper.readValue(requestBody, Map.class);
        return setParamsByRequest(map,keyValueMap);
    }

    /**
     * 从请求中设置参数
     * @param requestBody
     * @Param keyValue：存储需要参数化的key、和userName
     * @return
     */
    public static List<ParamsEntity> setParamsByRequest(Map<String,Object> requestBody,Map<String,String> keyValueMap) throws JsonProcessingException {
        List<ParamsEntity> list = new ArrayList<>();
        ParamsEntity entity = null;
        for (Map.Entry<String, String> stringStringEntry : keyValueMap.entrySet()) {
            String useName = stringStringEntry.getKey();
            String keyName = stringStringEntry.getValue();
            Object keyValue = requestBody.get(keyName);
            if (null == keyValue || "".equals(keyValue)) continue;
            entity  = new ParamsEntity(useName,"requestBody",keyName,keyValue);
            list.add(entity);
        }
        return list;
    }

    public static List<ParamsEntity> setNullParams() throws JsonProcessingException {
        List<ParamsEntity> list = new ArrayList<>();
        return list;
    }

}
