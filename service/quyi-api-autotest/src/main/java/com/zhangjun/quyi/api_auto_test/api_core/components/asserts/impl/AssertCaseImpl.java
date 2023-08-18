package com.zhangjun.quyi.api_auto_test.api_core.components.asserts.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.api_auto_test.api_core.components.asserts.AssertCase;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiAssertEntity;
import com.zhangjun.quyi.api_auto_test.api_core.enums.AssertEnums;
import com.zhangjun.quyi.api_auto_test.api_core.handler.ApiRunHandler;
import com.zhangjun.quyi.api_auto_test.util.JavaJaninoUtil;
import com.zhangjun.quyi.utils.JsonUtil;
import okhttp3.Headers;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * 断言实现类
 */
public class AssertCaseImpl implements AssertCase {

    private static Logger logger  = LoggerFactory.getLogger(AssertCaseImpl.class);


    /**
     * 基础判断
     * @return
     */
    public boolean baseAssert(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders, String responseBody) throws IOException {
        boolean flag = false;
        String type = apiAssertEntity.getType();
        if(type.equals(AssertEnums.AssertTypeEnum.EQUALS.value)) flag = this.eq(apiAssertEntity,code,responseHeaders, responseBody);
        else if (type.equals(AssertEnums.AssertTypeEnum.NOT_EQUALS.value)) flag = this.notEq(apiAssertEntity,code,responseHeaders, responseBody);
        else if (type.equals(AssertEnums.AssertTypeEnum.CONTAINS.value)) flag = this.contains(apiAssertEntity,code,responseHeaders, responseBody);
        else flag = this.notContains(apiAssertEntity,code,responseHeaders, responseBody);
        return flag;
    }

    /**
     * 判断相等
     * @param apiAssertEntity
     * @return
     */
    @Override
    public boolean eq(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody) throws IOException {
        boolean flag = false;
        String from = apiAssertEntity.getFrom();
        logger.debug("响应：" + responseBody);
        logger.debug("apiAssertEntity = " + apiAssertEntity);
        // 对比响应体字段
        if (from.equals(AssertEnums.AssertFormEnum.RESPONSE_BODY.value)){
            JsonNode lastNode = JsonUtil.getLastNode(responseBody, apiAssertEntity.getKey());
            logger.debug("从响应中拿到的值为：" + lastNode.asText());
            logger.debug("预期值为：" + apiAssertEntity.getExpectValue());
            flag = apiAssertEntity.getExpectValue().equals(lastNode.asText()) ? true : false;
        }
        // 对比响应码
        else if (from.equals(AssertEnums.AssertFormEnum.RESPONSE_CODE.value))
            flag  = apiAssertEntity.getExpectValue() == code ? true : false;
        return flag;
    }

    /**
     * 判断不相等
     * @param apiAssertEntity
     * @return
     */
    @Override
    public boolean notEq(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody) throws IOException {
        return !eq(apiAssertEntity,code,responseHeaders,responseBody);
    }

    /**
     * 判断包含
     * @param apiAssertEntity
     * @return
     */
    @Override
    public boolean contains(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody) {
        return false;
    }

    /**
     * 判断不包含
     * @param apiAssertEntity
     * @return
     */
    @Override
    public boolean notContains(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders,String responseBody) {
        return false;
    }

    /**
     * 判断是空
     * @param apiAssertEntity
     * @param code
     * @param responseHeaders
     * @param responseBody
     * @return
     */
    @Override
    public boolean isNull(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders, String responseBody) {
        return false;
    }

    /**
     * 判断不是空
     * @param apiAssertEntity
     * @param code
     * @param responseHeaders
     * @param responseBody
     * @return
     */
    @Override
    public boolean notIsNull(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders, String responseBody) {
        return false;
    }

    /**
     * 判断长度
     * @param apiAssertEntity
     * @param code
     * @param responseHeaders
     * @param responseBody
     * @return
     */
    @Override
    public boolean lengthEq(ApiAssertEntity apiAssertEntity, Integer code, Headers responseHeaders, String responseBody) {
        return false;
    }

}
