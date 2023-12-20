package com.zhangjun.quyi.currency_test.core.api.clientImpl;


import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.core.api.BaseApi;
import com.zhangjun.quyi.currency_test.utils.AssertUtil;
import com.zhangjun.quyi.currency_test.utils.BodyParamsBuilder;
import com.zhangjun.quyi.currency_test.utils.ParamsEntity;
import com.zhangjun.quyi.currency_test.utils.ParamsSetUtil;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferralCodeApi extends BaseApi {

    private static Logger logger = Logger.getLogger(GameApi.class);

    public ReferralCodeApi(String url) {
        super(url);
    }

    /**
     * 使用兑换码
     * @return
     */
    public ApiResultEntity useReferralCodeApi(List<ParamsEntity> paramsEntities,String referralCode) throws Exception {
        String url = this.url + "/api/v1/coupon/number/use";
        String requestBody = "{\n" +
                "    \"number\": \""+referralCode+"\",\n" +
                "    \"token\": \"${token}\",\n" +
                "    \"user_id\": \"${user_id}\"\n" +
                "}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        logger.info("使用兑换码-请求：" + requestBody);
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        logger.info("使用兑换码-响应：" + responseBody);
        return new ApiResultEntity("使用邀请码",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                JsonUtil.objectMapper.readValue(responseBody,Map.class),
                !AssertUtil.assertResponseTextEquals(responseBody, "message", "ok"),
                ParamsSetUtil.setNullParams());
    }
}
