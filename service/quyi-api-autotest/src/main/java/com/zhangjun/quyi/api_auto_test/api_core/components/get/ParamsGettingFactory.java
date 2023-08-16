package com.zhangjun.quyi.api_auto_test.api_core.components.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.impl.PathParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.impl.RequestBodyParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.impl.RequestHeaderParamsGetting;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数处理工厂
 */
public class ParamsGettingFactory {

    public static List<ParamsGetting> paramsGettings = new ArrayList<>();    // 设置参数列表

    /**
     * 构建ParamsGetting实现对象
     * @return
     */
    public static List<ParamsGetting> buildGettingObj(ApiTestCaseEntity apiTestCaseEntity) throws JsonProcessingException {
        if(ParamsGetting.ifPathNeedParams(apiTestCaseEntity)) paramsGettings.add(new PathParamsGetting());
        if (ParamsGetting.ifRequestHeadersNeedParams(apiTestCaseEntity)) paramsGettings.add(new RequestHeaderParamsGetting());
        if (ParamsGetting.ifRequestBodyNeedParams(apiTestCaseEntity)) paramsGettings.add(new RequestBodyParamsGetting());
        return ParamsGettingFactory.paramsGettings;
    }
}
