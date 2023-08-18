package com.zhangjun.quyi.api_auto_test.api_core.components.set.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.get.impl.PathParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.set.DivResponseParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsFromEnum;
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

    private static Logger logger  = LoggerFactory.getLogger(DivResponseParamsSettingImpl.class);


    /**
     * 参数设置实现方法
     * @return
     */
    @Override
    public ApiParamsEntity setParams(String responseBody, Headers headers,Object requestBody,Object requestHeaders, ApiParamsEntity apiParamsEntity) throws IOException {
        logger.debug("替换前的参数对象为:" + apiParamsEntity);
        // 从响应体中拿数据
        if (apiParamsEntity.getParamFrom().equals(ParamsFromEnum.RESPONSE_BODY.value))
            setByResponseBody(responseBody,apiParamsEntity);
        else if (apiParamsEntity.getParamFrom().equals(ParamsFromEnum.RESPONSE_HEADER.value))
            setByResponseHeader(headers,apiParamsEntity);
        else if (apiParamsEntity.getParamFrom().equals(ParamsFromEnum.REQUEST_HEADER.value))
            setByRequestHeader(requestHeaders,apiParamsEntity);
        else setByRequestBody(requestBody,apiParamsEntity);
        logger.debug("替换后的参数对象为:" + apiParamsEntity);
        DivResponseParamsSetting.ApiParamsEntitys.add(apiParamsEntity);
        return apiParamsEntity;
    }

    /**
     * 从请求体中设置
     * @param requestBody
     * @param apiParamsEntity
     * @return
     */
    private ApiParamsEntity setByRequestBody(Object requestBody, ApiParamsEntity apiParamsEntity) {
        String paramsEq = apiParamsEntity.getParamsEq();

        return null;
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

    /**
     * 从响应体中拿数据
     * @param apiParamsEntity
     * @return
     * @throws IOException
     */
    private ApiParamsEntity setByResponseBody(String responseBody,ApiParamsEntity apiParamsEntity){
        logger.debug("从响应体中拿到数据.....");
        // 从响应体中拿数据
        logger.debug("正则表达式为：" + apiParamsEntity.getParamsEq());
        String paramValue = RexUtils.getByRex(apiParamsEntity.getParamsEq(), responseBody);
        apiParamsEntity.setParamValue(paramValue);
        logger.debug("拿到真实数据为：" + apiParamsEntity);
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

    }
}
