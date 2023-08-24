package com.zhangjun.quyi.api_auto_test.api_core.components.set.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.get.impl.PathParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.set.DivResponseParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsFromEnum;
import com.zhangjun.quyi.api_auto_test.api_core.handler.ApiRunHandler;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import com.zhangjun.quyi.utils.RequestUtil;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数设置实现类
 */
public class DivResponseParamsSettingImpl implements DivResponseParamsSetting {


    static {
        LogStringBuilder.logger = LoggerFactory.getLogger(DivResponseParamsSettingImpl.class);
    }


    /**
     * 参数设置实现方法
     * @return
     */
    @Override
    public ApiParamsEntity setParams(String responseBody, Headers headers,Object requestBody,Object requestHeaders, ApiParamsEntity apiParamsEntity) throws IOException {
        LogStringBuilder.addLog("替换前的参数对象为:" + apiParamsEntity);
        // 从响应体中拿数据
        if (apiParamsEntity.getParamFrom().equals(ParamsFromEnum.RESPONSE_BODY.value))
            setByResponseBody(responseBody,apiParamsEntity);
        else if (apiParamsEntity.getParamFrom().equals(ParamsFromEnum.RESPONSE_HEADER.value))
            setByResponseHeader(headers,apiParamsEntity);
        else if (apiParamsEntity.getParamFrom().equals(ParamsFromEnum.REQUEST_HEADER.value))
            setByRequestHeader(requestHeaders,apiParamsEntity);
        else setByRequestBody(requestBody,apiParamsEntity);
        LogStringBuilder.addLog("替换后的参数对象为:" + apiParamsEntity);
        DivResponseParamsSetting.ApiParamsEntitys.add(apiParamsEntity);
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
        LogStringBuilder.addLog("正则表达式为：" + apiParamsEntity.getParamsEq());
        String paramValue = RexUtils.getByRex(apiParamsEntity.getParamsEq(), responseBody);
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
        LogStringBuilder.addLog("正则表达式为：" + apiParamsEntity.getParamsEq());
        String paramsEq = apiParamsEntity.getParamsEq();
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

    }
}
