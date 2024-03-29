package com.zhangjun.quyi.api_auto_test.api_core.components.param.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsEnums;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.utils.JsonUtil;

import java.util.List;

/**
 * 参数获取接口
 */
public interface ParamsGetting {

    /**
     * 判断是否需要获取参数
     * @param apiTestCaseEntity
     * @return
     */
    static boolean ifNeedGetParams(ApiTestCaseEntity apiTestCaseEntity) throws JsonProcessingException {
        return ifPathNeedParams(apiTestCaseEntity)
                || ifRequestBodyNeedParams(apiTestCaseEntity)
                || ifRequestHeadersNeedParams(apiTestCaseEntity);
    }

    /**
     * 判断地址是否需要参数
     * @param apiTestCaseEntity
     * @return
     */
    static boolean ifPathNeedParams(ApiTestCaseEntity apiTestCaseEntity){
        return apiTestCaseEntity.getApiPath().contains(ParamsEnums.ParamsSymbolEnum.API_PARAMS_SYMBOL.symbol)
                || apiTestCaseEntity.getApiPath().contains(ParamsEnums.ParamsSymbolEnum.SYSTEM_PARAMS_SYMBOL.symbol) ? true : false;
    }

    /**
     * 判断请求体是否需要参数
     * @param apiTestCaseEntity
     * @return
     * @throws JsonProcessingException
     */
    static boolean ifRequestBodyNeedParams(ApiTestCaseEntity apiTestCaseEntity) throws JsonProcessingException {
        return JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity.getRequestBody()).contains(ParamsEnums.ParamsSymbolEnum.API_PARAMS_SYMBOL.symbol) ||
                JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity.getRequestBody()).contains(ParamsEnums.ParamsSymbolEnum.SYSTEM_PARAMS_SYMBOL.symbol) ? true : false;
    }

    /**
     * 判断请求头是否需要参数
     * @param apiTestCaseEntity
     * @return
     * @throws JsonProcessingException
     */
    static boolean ifRequestHeadersNeedParams(ApiTestCaseEntity apiTestCaseEntity) throws JsonProcessingException {
        return JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity.getRequestHeaders()).contains(ParamsEnums.ParamsSymbolEnum.API_PARAMS_SYMBOL.symbol) ||
                JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity.getRequestHeaders()).contains(ParamsEnums.ParamsSymbolEnum.SYSTEM_PARAMS_SYMBOL.symbol) ? true : false;
    }

    /**
     * 处理参数化设置
     * @param sources
     * @param target
     * @return
     */
    ApiTestCaseEntity getParams(List<ApiParamsEntity> sources,ApiTestCaseEntity target) throws JsonProcessingException;


}
