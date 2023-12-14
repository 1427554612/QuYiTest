package com.zhangjun.quyi.currency_test.performance.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.utils.*;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口层
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBaseApi{

    public String url;
    public String platform;

    private static Logger logger = Logger.getLogger(BaseApi.class);


    /**
     * 后台登录
     * @return
     */
    public  ApiResultEntity adminLoginApi() throws Exception {
        String url = this.url + "/admin_auth/v1/public/login";
        String requestBody = "{\n" +
                "    \"username\": \"admin_super\",\n" +
                "    \"password\": \"123456\"\n" +
                "}";
        Map<String,Object> headers = new HashMap<>();
        headers.put("Content-Type","application/json; charset=utf-8");
        headers.put("User-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, headers);
        Map<String,String> setParamMap = new HashMap<String, String>();
        setParamMap.put("token", "data.token");
        logger.info("后台登录-响应:" + responseBody);
        return new ApiResultEntity("后台登录",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                ParamsSetUtil.setParamsByResponse(responseBody, setParamMap));
    }


    /**
     * 后台补单
     * @return
     */
    public ApiResultEntity repairOrderApi(List<ParamsEntity> ... paramsEntities) throws Exception {
        String url = this.url + "/rd1/v1/asset_order/repair_order";
        String requestBody = "{\n" +
                "    \"id\": \"${id}\",\n" +
                "    \"amount\": \"${amount}\"\n" +
                "}";
        Map<String,Object> headers = new HashMap<>();
        headers.put("Content-Type","application/json; charset=utf-8");
        headers.put("User-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        headers.put("Platform",this.platform);
        headers.put("token","${token}");
        String headerStr = JsonUtil.objectMapper.writeValueAsString(headers);
        requestBody = (String) new BodyParamsBuilder().parseParams(paramsEntities[0],requestBody);
        logger.info("补单接口-请求： " + requestBody);
        headerStr = (String) new BodyParamsBuilder().parseParams(paramsEntities[1],headerStr);
        headers = JsonUtil.objectMapper.readValue(headerStr,Map.class);

        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, headers);
        logger.info("补单接口-响应：" + responseBody);
        return new ApiResultEntity("后台补单",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                ParamsSetUtil.setNullParams());
    }

    /**
     * 发送站内信接口
     * @param paramsEntityList
     * @return
     * @throws JsonProcessingException
     */
    public ApiResultEntity sendMessageApi(List<ParamsEntity> paramsEntityList) throws Exception {
        String url =  this.url + "/rd1cfg/api/v1/admin_backend/asset_order/temporary/send";
        String requestBody = "{\n" +
                "    \"guid\": \"4AB7921211FE0EB6CC6BE852FD58329D\"\n" +
                "}";
        Map<String,Object> headers = new HashMap<>();
        headers.put("Content-Type","application/json; charset=utf-8");
        headers.put("Platform",this.platform);
        headers.put("token","${token}");
        String headerStr = JsonUtil.objectMapper.writeValueAsString(headers);
        headerStr = (String) new BodyParamsBuilder().parseParams(paramsEntityList,headerStr);
        headers = JsonUtil.objectMapper.readValue(headerStr,Map.class);
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, headers);
        logger.info("发送站内信-响应：" + responseBody);
        return new ApiResultEntity("发送站内信",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                ParamsSetUtil.setNullParams());
    }

    /**
     * 人工入款
     * @return
     */
    public ApiResultEntity artificialRecharge() throws IOException {
        String url = this.url + "/rd1/v1/asset_order/direct_recharge";
        String requestBody = "{\n" +
                "    \"user_id\": \"6564322d11e05e16a0638354\",\n" +
                "    \"recharge_type\": 0,\n" +
                "    \"amount\": 100,\n" +
                "    \"currency\": \"PHP\",\n" +
                "    \"money_type\": \"realmoney\"\n" +
                "}";
        Map<String,Object> headers = new HashMap<>();
        headers.put("Content-Type","application/json; charset=utf-8");
        headers.put("Platform",this.platform);
        headers.put("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluX3N1cGVyIiwiZXhwcmVzc19hdCI6ODY0MDAsImNyZWF0ZV9hdCI6MTcwMTA2NzI1OX0.40-Mufahj45uPqBzNSHckNr8mfGBww1wSkWOwT4uyFo");
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, headers);
        logger.info("人工入款接口：" + responseBody);
        return null;
    }

}
