package com.zhangjun.quyi.api_auto_test.api_core.components.param.get.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.api_core.components.param.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.param.set.ParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsEnums;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.constans.StrConstant;
import com.zhangjun.quyi.utils.JsonUtil;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 处理请求头中需要的参数
 */
public class RequestHeaderParamsGetting implements ParamsGetting {

    static {
        LogStringBuilder.logger = LoggerFactory.getLogger(RequestHeaderParamsGetting.class);
    }

    /**
     * 处理请求头
     * @param sources
     * @param target
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public ApiTestCaseEntity getParams(List<ApiParamsEntity> sources, ApiTestCaseEntity target) throws JsonProcessingException {
        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + target.getCaseName() + StrConstant.SYMBOL_COMMA + RequestHeaderParamsGetting.class.getName() + LogStringBuilder.PARAMS_HANDLE_RUN);
        String s = JsonUtil.objectMapper.writeValueAsString(target.getRequestHeaders());
        String finalHeaderStr = s;
        for (ParamsEnums.ParamsSymbolEnum paramsSymbolEnum : ParamsEnums.ParamsSymbolEnum.values()) {
            finalHeaderStr = RexUtils.getByCollection(finalHeaderStr, ParamsSetting.apiParamsEntitys,paramsSymbolEnum.symbol);
        }
        Object finalHeader = JsonUtil.objectMapper.readValue(finalHeaderStr, Object.class);
        target.setRequestBody(finalHeader);
        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + target.getCaseName() + StrConstant.SYMBOL_COMMA +  " replace data：" + target.getRequestHeaders());
        return target;
    }
}
