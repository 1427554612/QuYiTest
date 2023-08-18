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

    public static void main(String[] args) throws JsonProcessingException {
        String responseBody = "{\n" +
                "  \"message\": \"成功\",\n" +
                "  \"code\": 20000,\n" +
                "  \"data\": {\n" +
                "    \"testConfig\": {\n" +
                "      \"configId\": \"1692377513457664001\",\n" +
                "      \"configName\": \"zhanzj_0818\",\n" +
                "      \"configData\": {},\n" +
                "      \"configType\": \"布吉岛\",\n" +
                "      \"createTime\": \"2023-08-18 11:26:56\",\n" +
                "      \"updateTime\": \"2023-08-18 11:26:56\",\n" +
                "      \"updateUp\": \"张军\",\n" +
                "      \"configMark\": \"wwzzssddd\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"success\": true\n" +
                "}";
        JsonNode lastNode = getLastNode(responseBody, "data.testConfig.configName");
        System.out.println(lastNode.asText());
    }
}
