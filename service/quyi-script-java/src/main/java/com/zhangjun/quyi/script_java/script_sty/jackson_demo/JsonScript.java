package com.zhangjun.quyi.script_java.script_sty.jackson_demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * JackSon解析
 */
public class JsonScript {

    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\"code\":200,\"data\":{\"list\":[],\"total\":0,\"page\":1,\"pageSize\":100},\"msg\":\"OK\"}";
        HashMap<String,Object> hashMap = JsonUtil.objectMapper.readValue(json, HashMap.class);
        System.out.println(JsonUtil.objectMapper.writeValueAsString(hashMap));
    }
}
