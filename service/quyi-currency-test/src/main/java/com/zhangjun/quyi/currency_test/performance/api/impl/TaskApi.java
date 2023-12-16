package com.zhangjun.quyi.currency_test.performance.api.impl;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.performance.api.BaseApi;
import com.zhangjun.quyi.currency_test.utils.AssertUtil;
import com.zhangjun.quyi.currency_test.utils.BodyParamsBuilder;
import com.zhangjun.quyi.currency_test.utils.ParamsEntity;
import com.zhangjun.quyi.currency_test.utils.ParamsSetUtil;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import lombok.Data;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskApi extends BaseApi {

    private static Logger logger = Logger.getLogger(TaskApi.class);

    public TaskApi(String url) {
        super(url);
    }

    /**
     * 判断首登是否有奖励
     * @return
     */
    public ApiResultEntity myRewardApi(List<ParamsEntity> paramsEntities) throws Exception {
        String url = this.url + "/api/v1/first-login-task/my_reward";
        String requestBody = "{\n" +
                "    \"token\": \"${token}\",\n" +
                "    \"user_id\": \"${user_id}\"\n" +
                "}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        logger.info("判断是否有奖励-接口：" + responseBody);
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("biz_id","data.reward.biz_id");
        List<ParamsEntity> paramsEntityList = ParamsSetUtil.setParamsByResponse(responseBody, keyValueMap);
        return new ApiResultEntity("判断是否有奖励",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                JsonUtil.objectMapper.readValue(responseBody,Map.class),
                !AssertUtil.assertResponseTextEquals(responseBody, "data.reward.reward", 0),
                paramsEntityList);
    }

    /**
     * 领取首登奖励
     * @return
     */
    public ApiResultEntity useFirstLoginRewardApi(List<ParamsEntity>... paramsEntities) throws Exception {
        String url = this.url + "/api/v1/first-login-task/claim";
        String requestBody = "{\"token\":\"${token}\",\"user_id\":\"${user_id}\",\"biz_id\":\"${biz_id}\"}";
        for (List<ParamsEntity> paramsEntity : paramsEntities) {
            requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntity, requestBody);
        }
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        logger.info("领取首登奖励-接口：" + responseBody);
        return new ApiResultEntity("领取首登奖励",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                JsonUtil.objectMapper.readValue(responseBody,Map.class),
                AssertUtil.assertResponseTextEquals(responseBody, "message", "success"),
                ParamsSetUtil.setNullParams());
    }
}
