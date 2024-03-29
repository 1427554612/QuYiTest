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

import java.util.List;
import java.util.Map;

/**
 *
 */
public class GameApi extends BaseApi {

    private static Logger logger = Logger.getLogger(GameApi.class);

    public GameApi(String url) {
        super(url);
    }

    /**
     * dice投注
     * @return
     */
    public ApiResultEntity diceBet(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\n" +
                "    \"bet_amount\": 100,\n" +
                "    \"direction\": \"small\",\n" +
                "    \"prediction\": 50,\n" +
                "    \"token\": \"${token}\",\n" +
                "    \"user_id\": \"${user_id}\",\n" +
                "    \"currency\": \"BRL\"\n" +
                "}";
        String url = this.url + "/game/cryptos/bet";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        logger.info("投注dice-请求：" + requestBody);
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        logger.info("投注dice-响应：" + responseBody);
        return new ApiResultEntity("投注dice",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                JsonUtil.objectMapper.readValue(responseBody,Map.class),
                !AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                ParamsSetUtil.setNullParams());
    }
}
