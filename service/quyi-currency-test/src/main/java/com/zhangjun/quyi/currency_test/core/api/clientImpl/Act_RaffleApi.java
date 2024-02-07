package com.zhangjun.quyi.currency_test.core.api.clientImpl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.currency_test.core.api.BaseApi;
import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
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

/**
 * 转盘裂变活动api
 */
public class Act_RaffleApi extends BaseApi {

    private static Logger logger = Logger.getLogger(GameApi.class);

    public Act_RaffleApi(String url) {
        super(url);
    }

    /**
     * 点击转盘
     * @param
     * @return
     */
    public ApiResultEntity draw(String user_id,String token) throws Exception {
        String requestBody = "{\n" +
                "    \"user_id\": \""+user_id+"\",\n" +
                "    \"token\": \""+token+"\"\n" +
                "}";
        String url = this.url + "/api/v1/act-raffle/draw";
        logger.info("点击转盘-请求：" + requestBody);
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        logger.info("点击转盘-响应：" + responseBody);
        return new ApiResultEntity("点击转盘",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody, Map.class),
                JsonUtil.objectMapper.readValue(responseBody,Map.class),
                !AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                ParamsSetUtil.setNullParams());
    }

    /**
     * 查询详情
     * @param paramsEntities
     * @return
     */
    public ApiResultEntity info(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\n" +
                "    \"user_id\": \"${user_id}\",\n" +
                "    \"token\": \"${token}\"\n" +
                "}";
        String url = this.url + "/api/v1/act-raffle/info";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        logger.info("点击转盘-请求：" + requestBody);
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        logger.info("点击转盘-响应：" + responseBody);
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("biz_id","data.reward.biz_id");
        return new ApiResultEntity("点击转盘",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody, Map.class),
                JsonUtil.objectMapper.readValue(responseBody,Map.class),
                !AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                ParamsSetUtil.setParamsByResponse(responseBody,keyValueMap));
    }

    /**
     * 提款
     * @param
     * @return
     */
    public ApiResultEntity claim(String biz_id,String user_id, String token) throws Exception {
        String requestBody = "{\n" +
                "    \"biz_id\": \""+biz_id+"\",\n" +
                "    \"user_id\": \""+user_id+"\",\n" +
                "    \"token\": \""+token+"\"\n" +
                "}";
        String url = this.url + "/api/v1/act-raffle/claim";
        logger.info("点击转盘-地址：" + url);
        logger.info("点击转盘-请求：" + requestBody);
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        logger.info("点击转盘-响应：" + responseBody);
        return new ApiResultEntity("点击转盘",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody, Map.class),
                JsonUtil.objectMapper.readValue(responseBody,Map.class),
                !AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                ParamsSetUtil.setNullParams());
    }
}
