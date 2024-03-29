package com.zhangjun.quyi.api_auto_test.api_core.components.param.set.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.param.set.ParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsEnums;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import okhttp3.Headers;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 接口参数设置
 */
public class ApiParamsSetting implements ParamsSetting {


    static {
        LogStringBuilder.logger = LoggerFactory.getLogger(ApiParamsSetting.class);
    }


    /**
     * 参数设置实现方法
     * @return
     */
    @Override
    public ApiParamsEntity setParams(String responseBody, Headers headers,Object requestBody,Object requestHeaders, ApiParamsEntity apiParamsEntity) throws IOException {
        LogStringBuilder.addLog("替换前的参数对象为:" + apiParamsEntity);
        // 从响应体中拿数据
        if (apiParamsEntity.getParamFrom().equals(ParamsEnums.ParamsFromEnum.RESPONSE_BODY.value))
            setByResponseBody(responseBody,apiParamsEntity);
        else if (apiParamsEntity.getParamFrom().equals(ParamsEnums.ParamsFromEnum.RESPONSE_HEADER.value))
            setByResponseHeader(headers,apiParamsEntity);
        else if (apiParamsEntity.getParamFrom().equals(ParamsEnums.ParamsFromEnum.REQUEST_HEADER.value))
            setByRequestHeader(requestHeaders,apiParamsEntity);
        else setByRequestBody(requestBody,apiParamsEntity);
        LogStringBuilder.addLog("替换后的参数对象为:" + apiParamsEntity);
        ParamsSetting.apiParamsEntitys.add(apiParamsEntity);
        return apiParamsEntity;
    }

    /**
     * 从响应体中拿数据
     * @param apiParamsEntity
     * @return
     * @throws IOException
     */
    private ApiParamsEntity setByResponseBody(String responseBody,ApiParamsEntity apiParamsEntity){
        LogStringBuilder.addLog("从响应体中拿到数据.....");
        // 从响应体中拿数据
        LogStringBuilder.addLog("正则表达式为：" + apiParamsEntity.getParamEq());
        String paramValue = RexUtils.getByRex(apiParamsEntity.getParamEq(), responseBody);
        apiParamsEntity.setParamValue(paramValue);
        LogStringBuilder.addLog("拿到真实数据为：" + paramValue);
        return apiParamsEntity;
    }


    /**
     * 从请求体中设置
     * @param requestBody
     * @param apiParamsEntity
     * @return
     */
    private ApiParamsEntity setByRequestBody(Object requestBody, ApiParamsEntity apiParamsEntity) {
        LogStringBuilder.addLog("从请求体中拿到数据.....");
        LogStringBuilder.addLog("正则表达式为：" + apiParamsEntity.getParamEq());
        String paramsEq = apiParamsEntity.getParamEq();
        LogStringBuilder.addLog("请求参数为：" + requestBody.toString());
        String paramValue = RexUtils.getByRex(paramsEq, requestBody.toString());
        apiParamsEntity.setParamValue(paramValue);
        LogStringBuilder.addLog("替换后真实的值为：" + paramValue);
        return apiParamsEntity;
    }

    /**
     * 从请求头中设置
     * @param requestHeaders
     * @param apiParamsEntity
     * @return
     */
    private ApiParamsEntity setByRequestHeader(Object requestHeaders, ApiParamsEntity apiParamsEntity) {
        return null;
    }


    /**
     * 从响应头中拿数据
     * @param apiParamsEntity
     * @return
     */
    private ApiParamsEntity setByResponseHeader(Headers headers, ApiParamsEntity apiParamsEntity) {
        return null;
    }

}
