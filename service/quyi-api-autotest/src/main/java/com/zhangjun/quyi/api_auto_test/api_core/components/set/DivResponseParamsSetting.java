package com.zhangjun.quyi.api_auto_test.api_core.components.set;

import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsFromEnum;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

/**
 * 自定义参数设置接口
 */
public interface DivResponseParamsSetting {
    // 参数数组，保存存储的所有参数数据
//    List<ApiParamsEntity> ApiParamsEntitys = new ArrayList<>();
    List<ApiParamsEntity> ApiParamsEntitys = Arrays.asList(
            new ApiParamsEntity("login","_id", ParamsFromEnum.RESPONSE_BODY.value,"_id:(.*?),","1676169481063038977" )
            ,new ApiParamsEntity("login","token", ParamsFromEnum.RESPONSE_BODY.value,"token:(.*?),","1691070841078435841"));

    /**
     * 从响应中设置参数
     * @param response
     * @param apiParamsEntity
     * @return
     * @throws IOException
     */
    ApiParamsEntity setParams(Response response, ApiParamsEntity apiParamsEntity) throws IOException;


    /**
     * 从请求中设置参数
     * @param request
     * @param apiParamsEntity
     * @return
     */
    ApiParamsEntity setParams(Request request, ApiParamsEntity apiParamsEntity);
}
