package com.zhangjun.quyi.api_auto_test.api_core.components.set.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.set.DivResponseParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsFromEnum;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import com.zhangjun.quyi.utils.RequestUtil;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 参数设置实现类
 */
public class DivResponseParamsSettingImpl implements DivResponseParamsSetting {

    /**
     * 参数设置实现方法
     * @return
     */
    @Override
    public ApiParamsEntity setParams(Response response,ApiParamsEntity apiParamsEntity) throws IOException {
        ApiParamsEntity finalApiParamsEntity = null;
        // 从响应体中拿数据
        if (apiParamsEntity.getParamFrom().equals(ParamsFromEnum.RESPONSE_BODY.value))
            finalApiParamsEntity = setByResponseBody(response,apiParamsEntity);
        else if (apiParamsEntity.getParamFrom().equals(ParamsFromEnum.RESPONSE_CODE.value))
            finalApiParamsEntity = setByResponseCode(response,apiParamsEntity);
        DivResponseParamsSetting.ApiParamsEntitys.add(finalApiParamsEntity);
        return apiParamsEntity;
    }

    /**
     * 从请求中设置参数
     * @param request
     * @param apiParamsEntity
     * @return
     */
    @Override
    public ApiParamsEntity setParams(Request request, ApiParamsEntity apiParamsEntity) {
        return null;
    }


    /**
     * 从响应码中拿数据
     * @param response
     * @param apiParamsEntity
     * @return
     */
    private ApiParamsEntity setByResponseCode(Response response, ApiParamsEntity apiParamsEntity) {
        return null;
    }

    /**
     * 从响应头中拿数据
     * @param response
     * @param apiParamsEntity
     * @return
     */
    private ApiParamsEntity setByResponseHeader(Response response, ApiParamsEntity apiParamsEntity) {
        return null;
    }

    /**
     * 从响应体中拿数据
     * @param response
     * @param apiParamsEntity
     * @return
     * @throws IOException
     */
    private ApiParamsEntity setByResponseBody(Response response,ApiParamsEntity apiParamsEntity) throws IOException {

        // 从响应体中拿数据
        String responseBody = response.body().string();
        String paramValue = RexUtils.getByRex(apiParamsEntity.getParamsEq(), responseBody);
        apiParamsEntity.setParamValue(paramValue);
        return apiParamsEntity;
    }

    public static void main(String[] args) throws IOException {
        RequestUtil.setOkhttpClient(10,10);
        String requestBody = "{\n" +
                "  \"configData\": {},\n" +
                "  \"configMark\": \"zzzzsww\",\n" +
                "  \"configName\": \"伟大\",\n" +
                "  \"configType\": \"string\",\n" +
                "  \"updateUp\": \"string\"\n" +
                "}";
        Response response = (Response)RequestUtil.sendingRequest("http://localhost:8002/api/test_config/saveTestConfig", "POST", requestBody, null)[1];
        ApiParamsEntity apiParamsEntity = new ApiParamsEntity("saveTestConfig", "configId", "responseBody", "configId\":\"(.*?)\",\"", null);
        ApiParamsEntity finalApiParamsEntity = new DivResponseParamsSettingImpl().setParams(response, apiParamsEntity);
        System.out.println(finalApiParamsEntity);

    }
}
