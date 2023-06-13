package com.zhangjun.quyi.currency_test.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.currency_test.utils.*;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyTestCase {
    private static Integer MAX_REQUEST = 50;
    private static Integer MAX_CONNECTIONS = 10000;
    private static String BASE_URL = "https://mexlucky-api.pre-release.xyz";
    private static ArrayList<Map<String,String>> keyValueList = new ArrayList();
    public static volatile CountDownLatch countDownLatch = null;      // 计数器


    @BeforeSuite
    public void init(){
        RequestUtil.setOkhttpClient(MAX_REQUEST,MAX_CONNECTIONS);
    }


    public static List<ParamsEntity> registerTest() throws Exception {
        String requestBody = "{\n" +
                "                \"account\": \"zj_"+System.currentTimeMillis() + Thread.currentThread().getName()+"@qq.com\",\n" +
                "                \"password\": \"zj123456\",\"grecaptcha_token\":\"03AL8dmw-ZnbbBgkXcVzUF_-jxNqnuCv9ze8R3bJ5fTxNdQqY5dzTkYrFIJfX0d2AjPysGU00v5dJxo3mYjW5XghxCCI_e1-VQPWdUiK4G6ie-weOVIQ6zd6h8JUhX65Ccww2uC3T-BRpG7uPk6Id2wxTo2SbcjHeIt8sbmJx8g1SYgCK_4NLSlSaRcS3gRw_gVm67sN0oRTdvzCCPUDsWyc2JOU1MEwzO1y4dLiTrrKf1IDn-AboejqbnPF7OT8Fx0jIdF-VLz0QoD5haX5QqD53HuviESQgZhyphCBG10LYfiCt13urUXg_Oqz6RNSlcFIVOvXczqrukxy0BUi6hIl18DIgidsdUKCcQrX052kDyNFa3dkwMY8UJL0H7tE-sHrh3HX1sn5eYtI13xIENouqHIsCy1sbJF8L-BWrHr6dbOCGgiGPZiK00LqbddVDLD9z8Pphv3pUGcJ2UVBeIgy6G5iQc7iXwsl7UdWKHDI1t6yYj_2_ath9j9o9eoZKUnqvPaDQu__QRyUb9hq4v6M0W-xoxCRunVAKL_mO6wEsZuCwZh8l554pjybHMLXfXLkdIEF75iN_D\"}";
        System.out.println("register request :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/user/register", "POST", requestBody, null);
        HashMap<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("account","account");
        keyValueMap.put("password","password");
        keyValueMap.put("grecaptcha_token","grecaptcha_token");
//        keyValueMap.put("grecaptcha_token","grecaptcha_token");
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data._id")) throw new Exception("断言错误：data._id");
        System.out.println("register response :" + responseBody);
        return ParamsSetUtil.setParamsByRequest(requestBody,keyValueMap);
    }

    @Test
    public static List<ParamsEntity> loginTest(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\"account\":\"${account}\",\"password\":\"${password}\",\"grecaptcha_token\":\"${grecaptcha_token}\"}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        System.out.println("login request :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/user/login", "POST", requestBody, null);
        System.out.println("login response :" + responseBody);
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("token","data.token");
        keyValueMap.put("user_id","data.user._id");
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data.token")) throw new Exception("断言错误：data.token");
        return ParamsSetUtil.setParamsByResponse(responseBody,keyValueMap);
    }


    @Test
    public static List<ParamsEntity> rechargeTest(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\n" +
                "\t\"amount\": 500,\n" +
                "\t\"task_id\": \"-1\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"pay_method\": \"mex\",\n" +
                "\t\t\"typ\": \"BANK\"\n" +
                "\t},\n" +
                "\t\"token\": \"${token}\",\n" +
                "\t\"user_id\": \"${user_id}\"\n" +
                "}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        System.out.println("recharge requestBody :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/user/recharge", "POST", requestBody, null);
        System.out.println("recharge response :" + responseBody);
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data.order_id")) throw new Exception("断言错误：data.token");
        return ParamsSetUtil.setNullParams();
    }

    @Test
    public static List<ParamsEntity> betTest(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\n" +
                "\t\"amount\": 500,\n" +
                "\t\"task_id\": \"-1\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"pay_method\": \"mex\",\n" +
                "\t\t\"typ\": \"BANK\"\n" +
                "\t},\n" +
                "\t\"token\": \"${token}\",\n" +
                "\t\"user_id\": \"${user_id}\"\n" +
                "}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        System.out.println("recharge requestBody :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/user/recharge", "POST", requestBody, null);
        System.out.println("recharge response :" + responseBody);
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data.order_id")) throw new Exception("断言错误：data.token");
        return ParamsSetUtil.setNullParams();
    }

    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        RequestUtil.setOkhttpClient(MAX_REQUEST,MAX_CONNECTIONS);
        CurrencyTestCase.countDownLatch = new CountDownLatch(MAX_REQUEST);
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_REQUEST);
        for (Integer i = 0; i < MAX_REQUEST; i++) {
            executorService.execute(()->{
                try {
                    List<ParamsEntity> registerParamsEntity = registerTest();
                    List<ParamsEntity> loginParamsEntity = loginTest(registerParamsEntity);
                    rechargeTest(loginParamsEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CurrencyTestCase.countDownLatch.countDown();
            });
        }
        CurrencyTestCase.countDownLatch.await();
        System.out.println("last = " +JsonUtil.objectMapper.writeValueAsString(ParamsList.getParamsList()));
        executorService.shutdown();
    }

}
