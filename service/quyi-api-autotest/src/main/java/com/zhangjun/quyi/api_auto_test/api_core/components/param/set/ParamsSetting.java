package com.zhangjun.quyi.api_auto_test.api_core.components.param.set;

import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import okhttp3.Headers;

import java.io.IOException;
import java.util.*;

/**
 * 自定义参数设置接口
 */
@FunctionalInterface
public interface ParamsSetting {

    // 参数数组，保存存储的所有参数数据
    List<ApiParamsEntity> apiParamsEntitys = new ArrayList<>();

    /**
     * 设置参数
     * @return
     * @throws IOException
     */
    ApiParamsEntity setParams(String responseBody, Headers headers,Object requestBody,Object requestHeaders, ApiParamsEntity apiParamsEntity) throws IOException;


}
