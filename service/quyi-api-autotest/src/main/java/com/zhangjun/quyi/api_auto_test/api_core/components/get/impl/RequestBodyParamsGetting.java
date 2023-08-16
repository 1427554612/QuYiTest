package com.zhangjun.quyi.api_auto_test.api_core.components.get.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 处理请求体中需要的参数
 */
public class RequestBodyParamsGetting implements ParamsGetting {

    private static Logger logger  = LoggerFactory.getLogger(RequestBodyParamsGetting.class);

    /**
     * 处理请求体
     * @param sources
     * @param target
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public ApiTestCaseEntity getParams(List<ApiParamsEntity> sources, ApiTestCaseEntity target) throws JsonProcessingException {
        logger.debug(target.getCaseName() + " 开始处理，" + RequestBodyParamsGetting.class.getName());
        String s = JsonUtil.objectMapper.writeValueAsString(target.getRequestBody());
        String finalBodyStr = RexUtils.getByCollection(s, sources);
        Object finalBody = JsonUtil.objectMapper.readValue(finalBodyStr, Object.class);
        logger.debug("替换之后的body = " + JsonUtil.objectMapper.writeValueAsString(finalBody));
        target.setRequestBody(finalBody);
        logger.debug(target.getCaseName() + "替换后的用例对象：" + target);
        return target;
    }
}
