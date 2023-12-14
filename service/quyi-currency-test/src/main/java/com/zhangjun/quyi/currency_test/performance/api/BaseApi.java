package com.zhangjun.quyi.currency_test.performance.api;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.performance.utils.ContainerUtil;
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

import java.util.*;
import java.util.stream.Collectors;

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
    public ApiResultEntity registerApi(String personId) throws Exception {
        String url = this.url  + "/user/register";
        String requestBody = "{\n" +
                "                \"account\": \"zj_"+System.currentTimeMillis() + Thread.currentThread().getName()+"@qq.com\",\n" +
                "                \"password\": \"zj123456\"," +
                "                \"person\":\""+personId+"\","+
                "\"grecaptcha_token\":\"FAKE_TOKEN\"}";
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(url, "POST", requestBody, null);
        HashMap<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("account","account");
        keyValueMap.put("password","password");
        keyValueMap.put("grecaptcha_token","grecaptcha_token");
        keyValueMap.put("user_id","data._id");
        if (!"".equals(personId)) keyValueMap.put("person","person");
        logger.info("上级id：" + personId);
        logger.info("注册接口-账号："+ JsonUtil.objectMapper.readValue(requestBody, Map.class));
        logger.info("注册接口-响应：" + responseBody);
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data._id")) throw new Exception("断言错误：data._id");
        List<ParamsEntity> paramsEntityList = ParamsSetUtil.setParamsByRequest(requestBody, keyValueMap);
        List<ParamsEntity> paramsEntityList1 = ParamsSetUtil.setParamsByResponse(responseBody, keyValueMap);
        paramsEntityList.addAll(paramsEntityList1);
        paramsEntityList = ContainerUtil.distinct(paramsEntityList);
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
        String requestBody = getPlatformBody();
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        String url = this.url + "/user/recharge";
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.sendRequest(  url, "POST", requestBody, null);
        long en = System.currentTimeMillis();
        logger.info("充值接口-响应：" + responseBody);
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("amount","amount");
        keyValueMap.put("id","data.order_id");
        List<ParamsEntity> paramsEntityList = ParamsSetUtil.setParamsByRequest(JsonUtil.objectMapper.readValue(requestBody, Map.class), keyValueMap);
        try {
            List<ParamsEntity> paramsEntityList1 = ParamsSetUtil.setParamsByResponse(responseBody, keyValueMap);
            paramsEntityList.addAll(paramsEntityList1);
        }catch (Exception e){
        }
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

    /**
     * 获取请求体
     * @return
     */
    private String getPlatformBody(){
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
        if (this.url.contains("b1-api")){
            requestBody = "{\n" +
                    "    \"user_id\": \"${user_id}\",\n" +
                    "    \"token\": \"${token}\",\n" +
                    "    \"currency\": \"BRL\",\n" +
                    "    \"amount\": \"10000\",\n" +
                    "    \"task_id\": \"-1\",\n" +
                    "    \"data\": {\n" +
                    "        \"name\": \"zhangjun\",\n" +
                    "        \"pix\": \"36464134175\",\n" +
                    "        \"email\": \"aaa@163.com\",\n" +
                    "        \"phone\": \"55632632521\",\n" +
                    "        \"typ\": \"PIXQR\",\n" +
                    "        \"pay_method\": \"pix\"\n" +
                    "    }\n" +
                    "}";
        }else if (this.url.contains("n1-api")){
            requestBody = "{\n" +
                    "    \"grecaptcha_token\": \"FAKE_TOKEN\",\n" +
                    "    \"user_id\": \"${user_id}\",\n" +
                    "    \"token\": \"${token}\",\n" +
                    "    \"currency\": \"NGN\",\n" +
                    "    \"amount\": \"10000\",\n" +
                    "    \"task_id\": \"-1\",\n" +
                    "    \"data\": {\n" +
                    "        \"typ\": \"BANK\",\n" +
                    "        \"pay_method\": \"electronic_wallet\"\n" +
                    "    },\n" +
                    "    \"adjust\": {\n" +
                    "        \"adid\": \"be7247ac7f75546291c8de287bce64f7\",\n" +
                    "        \"web_uuid\": \"c8e41672-011a-44a8-178c-0830a9a01192\",\n" +
                    "        \"gps_adid\": \"\",\n" +
                    "        \"external_id\": \"\",\n" +
                    "        \"fbc\": \"\",\n" +
                    "        \"fbp\": \"\"\n" +
                    "    }\n" +
                    "}";
        }else if (this.url.contains("b3-api")){
            requestBody="{\n" +
                    "    \"grecaptcha_token\": \"FAKE_TOKEN\",\n" +
                    "    \"user_id\": \"${user_id}\",\n" +
                    "    \"token\": \"${token}\",\n" +
                    "    \"currency\": \"BRL\",\n" +
                    "    \"amount\": \"1000\",\n" +
                    "    \"task_id\": \"-1\",\n" +
                    "    \"data\": {\n" +
                    "        \"name\": \"zhangjun\",\n" +
                    "        \"pix\": \"34686201950\",\n" +
                    "        \"email\": \"aaa@163.com\",\n" +
                    "        \"phone\": \"55231263232\",\n" +
                    "        \"typ\": \"PIXQR\",\n" +
                    "        \"pay_method\": \"pix\"\n" +
                    "    }\n" +
                    "}";
        }else if (this.url.contains("philucky-api")){
            requestBody = "{\n" +
                    "    \"grecaptcha_token\": \"FAKE_TOKEN\",\n" +
                    "    \"user_id\": \"${user_id}\",\n" +
                    "    \"token\": \"${token}\",\n" +
                    "    \"currency\": \"PHP\",\n" +
                    "    \"amount\": \"1000\",\n" +
                    "    \"task_id\": \"-1\",\n" +
                    "    \"data\": {\n" +
                    "        \"typ\": \"PAYMAYA\",\n" +
                    "        \"pay_method\": \"electronic_wallet\"\n" +
                    "    }\n" +
                    "}";
        }else if (this.url.contains("api.kelucky")){
            requestBody = "{\n" +
                    "    \"user_id\": \"${user_id}\",\n" +
                    "    \"token\": \"${token}\",\n" +
                    "    \"currency\": \"KES\",\n" +
                    "    \"amount\": \"10000\",\n" +
                    "    \"task_id\": \"-1\",\n" +
                    "    \"data\": {\n" +
                    "        \"phone\": \"768338531\",\n" +
                    "        \"typ\": \"M-PESA\",\n" +
                    "        \"pay_method\": \"electronic_wallet\"\n" +
                    "    }\n" +
                    "}";
        }
        return requestBody;

    }
}
