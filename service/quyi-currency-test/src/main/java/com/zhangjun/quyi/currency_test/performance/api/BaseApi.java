package com.zhangjun.quyi.currency_test.performance.api;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.utils.AssertUtil;
import com.zhangjun.quyi.currency_test.utils.BodyParamsBuilder;
import com.zhangjun.quyi.currency_test.utils.ParamsEntity;
import com.zhangjun.quyi.currency_test.utils.ParamsSetUtil;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础api类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseApi {

    public String url;   // url地址

    private static Logger logger = Logger.getLogger(BaseApi.class);

    /**
     * 注册接口
     * @return
     * @throws Exception
     */
    public ApiResultEntity registerApi() throws Exception {
        String url = this.url  + "/user/register";
        String requestBody = "{\n" +
                "                \"account\": \"zj_"+System.currentTimeMillis() + Thread.currentThread().getName()+"@qq.com\",\n" +
                "                \"password\": \"zj123456\"," +
                "                \"person\":\"643e2c58571fc3f2eccbdbcd\","+
                "\"grecaptcha_token\":\"FAKE_TOKEN\"}";
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        HashMap<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("account","account");
        keyValueMap.put("password","password");
        keyValueMap.put("grecaptcha_token","grecaptcha_token");
        logger.info("注册接口-响应：" + responseBody);
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data._id")) throw new Exception("断言错误：data._id");
        List<ParamsEntity> paramsEntityList = ParamsSetUtil.setParamsByRequest(requestBody, keyValueMap);
        logger.info("注册接口-参数："+ JsonUtil.objectMapper.readValue(requestBody, Map.class));
        return new ApiResultEntity("注册接口",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                paramsEntityList);
    }

    /**
     * 登录接口
     * @param paramsEntities
     * @return
     * @throws Exception
     */
    public ApiResultEntity loginApi(List<ParamsEntity> paramsEntities) throws Exception {
        String url = this.url +  "/user/login";
        String requestBody = "{\"account\":\"${account}\",\"password\":\"${password}\",\"grecaptcha_token\":\"${grecaptcha_token}\"}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        String responseBody = null;
        List<ParamsEntity> paramsEntityList = null;
        Map<String,Object> headers = new HashMap<>();
        headers.put("Devicetype","android_app");
        headers.put("Accounttype","email");
        long st = System.currentTimeMillis();
        try {
            responseBody = RequestUtil.sendRequest(url, "POST", requestBody, headers);
            Map<String, String> keyValueMap = new HashMap<>();
            keyValueMap.put("token","data.token");
            keyValueMap.put("user_id","data.user._id");
            if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data.token")) throw new Exception("断言错误：data.token");
            paramsEntityList = ParamsSetUtil.setParamsByResponse(responseBody, keyValueMap);
        }catch (Exception e){
            e.printStackTrace();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("errorMessage",e.getMessage());
            return new ApiResultEntity("登录接口",url,st,System.currentTimeMillis(),errorMap, false, null);
        }
        logger.info("登录接口-响应：" + responseBody);
        return new ApiResultEntity("登录接口",
                url,
                st,
                System.currentTimeMillis(),
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                paramsEntityList);
    }

    /**
     * 充值接口
     * @param paramsEntities
     * @return
     * @throws Exception
     */
    public  ApiResultEntity rechargeApi(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\n" +
                "    \"user_id\": \"${user_id}\",\n" +
                "    \"token\": \"${token}\",\n" +
                "    \"currency\": \"MXN\",\n" +
                "    \"amount\": \"1000\",\n" +
                "    \"task_id\": \"-1\",\n" +
                "    \"data\": {\n" +
                "        \"typ\": \"SPEI(Personnal)\",\n" +
                "        \"pay_method\": \"mex\"\n" +
                "    }\n" +
                "}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        logger.info("充值接口-请求体：" + requestBody);
        String url = this.url + "/user/recharge";
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(  url, "POST", requestBody, null);
        long en = System.currentTimeMillis();
        logger.info("充值接口-响应：" + responseBody);
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("amount","amount");
        keyValueMap.put("id","data.order_id");
        List<ParamsEntity> paramsEntityList = ParamsSetUtil.setParamsByRequest(JsonUtil.objectMapper.readValue(requestBody, Map.class), keyValueMap);
        List<ParamsEntity> paramsEntityList1 = ParamsSetUtil.setParamsByResponse(responseBody, keyValueMap);
        paramsEntityList.addAll(paramsEntityList1);
        for (int i = 0; i < paramsEntityList.size(); i++) {
            Object keyValue = paramsEntityList.get(i).getKeyValue();
            if (null == keyValue || "".equals(keyValue)) paramsEntityList.remove(i);
        }
        return new ApiResultEntity("充值接口",
                url,
                st,
                en,
                JsonUtil.objectMapper.readValue(requestBody,Map.class),
                AssertUtil.assertResponseTextEquals(responseBody, "code", 200),
                paramsEntityList);
    }
}
