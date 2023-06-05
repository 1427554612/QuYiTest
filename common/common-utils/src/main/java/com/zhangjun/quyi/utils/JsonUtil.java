package com.zhangjun.quyi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取最后一个node节点
     * @param responseBody
     * @param key
     * @return
     * @throws JsonProcessingException
     */
    public static JsonNode getLastNode(String responseBody, String key) throws JsonProcessingException {
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(responseBody);
        if (key.contains(".")){
            String[] splits = key.split("\\.");
            for(int i=0;i<splits.length;++i){
                jsonNode = jsonNode.get(splits[i]);
            }
        }else {
            if (jsonNode.isInt()) return jsonNode;
            else jsonNode = jsonNode.get(key);
        }
        return jsonNode;
    }
}
