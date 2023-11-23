package com.zhangjun.quyi.currency_test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.constans.PressureConstant;
import com.zhangjun.quyi.utils.JsonUtil;

public class AssertUtil {


    /**
     * 验证响应体字段是否等于预计的值
     * @param responseBody:响应体
     * @param key：断言字段
     * @param text：预计值
     * @return
     */
    public static boolean assertResponseTextEquals(String responseBody,String key,Object text) throws JsonProcessingException {
        JsonNode jsonNode = JsonUtil.getLastNode(responseBody,key);
        boolean flags = false;
        String newText = null;
        if (text instanceof Integer) newText = String.valueOf(text);
        if (jsonNode.isInt()) {
            flags =  String.valueOf(jsonNode.intValue()).equals(newText) ? PressureConstant.ASSERT_TRUE : PressureConstant.ASSERT_FALSE;
            return flags;
        }
        flags = jsonNode.asText().equals(text) ? PressureConstant.ASSERT_TRUE : PressureConstant.ASSERT_FALSE;
        return flags;
    }


    /**
     * 验证某个字段是否是null值
     * @param responseBody
     * @param key
     * @return
     * @throws JsonProcessingException
     */
    public static boolean assertResponseTextIsNull(String responseBody,String key) throws JsonProcessingException {
        JsonNode jsonNode = JsonUtil.getLastNode(responseBody,key);
        return jsonNode.asText() == null || jsonNode.asText().equals("null") || jsonNode.asText().equals("")? PressureConstant.ASSERT_TRUE : PressureConstant.ASSERT_FALSE;
    }


    /**
     * 验证某个字段是不是null值
     * @param responseBody
     * @param key
     * @return
     * @throws JsonProcessingException
     */
    public static boolean assertResponseTextNotIsNull(String responseBody,String key) throws JsonProcessingException {
        return assertResponseTextIsNull(responseBody,key) ? PressureConstant.ASSERT_FALSE : PressureConstant.ASSERT_TRUE;
    }



}
