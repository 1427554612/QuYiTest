package com.zhangjun.quyi.api_auto_test.api_core.components.get.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.handler.ApiRunHandler;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.constans.StrConstant;
import com.zhangjun.quyi.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 处理请求头中需要的参数
 */
public class RequestHeaderParamsGetting implements ParamsGetting {

    private static Logger logger  = LoggerFactory.getLogger(RequestBodyParamsGetting.class);

    /**
     * 处理请求头
     * @param sources
     * @param target
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public ApiTestCaseEntity getParams(List<ApiParamsEntity> sources, ApiTestCaseEntity target) throws JsonProcessingException {
        ApiRunHandler.logAdd(LogStringBuilder.CASE_NAME + target.getCaseName() + StrConstant.SYMBOL_COMMA + RequestHeaderParamsGetting.class.getName() + LogStringBuilder.PARAMS_HANDLE_RUN);
        String s = JsonUtil.objectMapper.writeValueAsString(target.getRequestHeaders());
        String finalHeaderStr = RexUtils.getByCollection(s, sources);
        Object finalHeader = JsonUtil.objectMapper.readValue(finalHeaderStr, Object.class);
        target.setRequestBody(finalHeader);
        ApiRunHandler.logAdd(LogStringBuilder.CASE_NAME + target.getCaseName() + StrConstant.SYMBOL_COMMA +  " replace data：" + target.getRequestHeaders());
        return target;
    }
}
