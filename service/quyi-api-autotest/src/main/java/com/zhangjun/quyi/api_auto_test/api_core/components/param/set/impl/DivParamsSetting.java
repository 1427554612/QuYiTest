package com.zhangjun.quyi.api_auto_test.api_core.components.param.set.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.param.set.ParamsSetting;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import okhttp3.Headers;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 自定义参数设置
 */
public class DivParamsSetting implements ParamsSetting {

    static {
        LogStringBuilder.logger = LoggerFactory.getLogger(DivParamsSetting.class);
    }

    @Override
    public ApiParamsEntity setParams(String responseBody, Headers headers, Object requestBody, Object requestHeaders, ApiParamsEntity apiParamsEntity) throws IOException {
        //TODO:自定义参数实现，走数据库
        return null;
    }
}
