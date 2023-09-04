package com.zhangjun.quyi.api_auto_test.api_core.components.param.set.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.param.set.ParamsSetting;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import okhttp3.Headers;

import java.io.IOException;

/**
 * 系统参数设置
 */
public class SystemParamsSetting implements ParamsSetting {

    @Override
    public ApiParamsEntity setParams(String responseBody, Headers headers, Object requestBody, Object requestHeaders, ApiParamsEntity apiParamsEntity) throws IOException {
        //TODO:系统参数设置
        return null;
    }
}
