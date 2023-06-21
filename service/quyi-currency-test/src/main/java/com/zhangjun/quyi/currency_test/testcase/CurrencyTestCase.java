package com.zhangjun.quyi.currency_test.testcase;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.currency_test.utils.*;
import com.zhangjun.quyi.utils.DateTimeUtil;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import okhttp3.OkHttpClient;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyTestCase {
    private static Integer MAX_CONNECTIONS = 10000;
    private static String BASE_URL = "https://mexlucky-api.pre-release.xyz";
    private static ArrayList<Map<String,String>> keyValueList = new ArrayList();
    public static volatile CountDownLatch countDownLatch = null;      // 计数器
    public static ExecutorService executorService = null;
    private static Logger logger = Logger.getLogger(CurrencyTestCase.class);
    private static BufferedWriter bf = null;

    static {
        try {
            bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:/id.txt"),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void init(int requestNumber){
        OkHttpClient okHttpClient = RequestUtil.setOkhttpClient(requestNumber, MAX_CONNECTIONS);
        CurrencyTestCase.countDownLatch = new CountDownLatch(requestNumber);
        executorService = Executors.newFixedThreadPool(requestNumber);
        logger.info(okHttpClient);
        logger.info(executorService);
        logger.info(countDownLatch);
    }

    public static void close() throws IOException {
        bf.close();
        CurrencyTestCase.executorService.shutdown();
        CurrencyTestCase.countDownLatch = null;
        CurrencyTestCase.executorService = null;
    }


    public static List<ParamsEntity> registerTest() throws Exception {
        String requestBody = "{\n" +
                "                \"account\": \"zj_"+System.currentTimeMillis() + Thread.currentThread().getName()+"@qq.com\",\n" +
                "                \"password\": \"zj123456\",\"grecaptcha_token\":\"03AL8dmw-ZnbbBgkXcVzUF_-jxNqnuCv9ze8R3bJ5fTxNdQqY5dzTkYrFIJfX0d2AjPysGU00v5dJxo3mYjW5XghxCCI_e1-VQPWdUiK4G6ie-weOVIQ6zd6h8JUhX65Ccww2uC3T-BRpG7uPk6Id2wxTo2SbcjHeIt8sbmJx8g1SYgCK_4NLSlSaRcS3gRw_gVm67sN0oRTdvzCCPUDsWyc2JOU1MEwzO1y4dLiTrrKf1IDn-AboejqbnPF7OT8Fx0jIdF-VLz0QoD5haX5QqD53HuviESQgZhyphCBG10LYfiCt13urUXg_Oqz6RNSlcFIVOvXczqrukxy0BUi6hIl18DIgidsdUKCcQrX052kDyNFa3dkwMY8UJL0H7tE-sHrh3HX1sn5eYtI13xIENouqHIsCy1sbJF8L-BWrHr6dbOCGgiGPZiK00LqbddVDLD9z8Pphv3pUGcJ2UVBeIgy6G5iQc7iXwsl7UdWKHDI1t6yYj_2_ath9j9o9eoZKUnqvPaDQu__QRyUb9hq4v6M0W-xoxCRunVAKL_mO6wEsZuCwZh8l554pjybHMLXfXLkdIEF75iN_D\"}";
        logger.info("api register request :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/user/register", "POST", requestBody, null);
        HashMap<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("account","account");
        keyValueMap.put("password","password");
        keyValueMap.put("grecaptcha_token","grecaptcha_token");
//        keyValueMap.put("grecaptcha_token","grecaptcha_token");
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data._id")) throw new Exception("断言错误：data._id");
        logger.info("api register response :" + responseBody);
        return ParamsSetUtil.setParamsByRequest(requestBody,keyValueMap);
    }


    public static List<ParamsEntity> loginTest(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\"account\":\"${account}\",\"password\":\"${password}\",\"grecaptcha_token\":\"${grecaptcha_token}\"}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        logger.info("api login request :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/user/login", "POST", requestBody, null);
        logger.info("api login response :" + responseBody);
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("token","data.token");
        keyValueMap.put("user_id","data.user._id");
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data.token")) throw new Exception("断言错误：data.token");
        return ParamsSetUtil.setParamsByResponse(responseBody,keyValueMap);
    }



    public static List<ParamsEntity> rechargeTest(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\n" +
                "    \"amount\": 1000,\n" +
                "    \"task_id\": \"-1\",\n" +
                "    \"data\": {\n" +
                "        \"pay_method\": \"mex\",\n" +
                "        \"typ\": \"CODI\"\n" +
                "    },\n" +
                "    \"token\": \"${token}\",\n" +
                "    \"user_id\": \"${user_id}\"\n" +
                "}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        logger.info("api recharge requestBody :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/user/recharge", "POST", requestBody, null);
        logger.info("api recharge response :" + responseBody);
        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data.order_id")) throw new Exception("断言错误：data.token");
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("cashier","data.pay_method.cashier");
        return ParamsSetUtil.setParamsByResponse(responseBody,keyValueMap);
    }

    public static List<ParamsEntity> notifyTest(List<ParamsEntity> paramsEntities) throws Exception {
        String url = "${cashier}";
        url = (String)new PathParamsBuilder().parseParams(paramsEntities, url);
        logger.info("api notify url :" + url);
        String responseBody = RequestUtil.sendRequest(url, "GET", (String) null, null);
        logger.info("api notify response :" + responseBody);
        return ParamsSetUtil.setNullParams();
    }

    public static List<ParamsEntity> betTest(List<ParamsEntity> paramsEntities) throws Exception {
        String requestBody = "{\n" +
                "  \"bet_amount\":1000 ,\n" +
                "  \"direction\": \"small\",\n" +
                "  \"prediction\": 95,\n" +
                "  \"token\": \"${token}\",\n" +
                "  \"player_id\": \"${user_id}\"\n" +
                "}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        logger.info("api bet requestBody :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/game/dice/bet", "POST", requestBody, null);
        if (AssertUtil.assertResponseTextEquals(responseBody,"code",200)){
            String player_id = JsonUtil.objectMapper.readTree(requestBody).get("player_id").textValue();
            logger.info("user_id ：" + player_id);
            bf.write("\"" + player_id +"\""+",");
            bf.flush();
            logger.info("api bet response :" + responseBody);
        }else {
            Exception error = new Exception("betTest 断言错误");
            logger.error(error + responseBody);
            throw error;
        }
//        if (!AssertUtil.assertResponseTextNotIsNull(responseBody,"data.order_id")) throw new Exception("断言错误：data.token");
        return ParamsSetUtil.setNullParams();
    }

    public static List<ParamsEntity> withdraw(List<ParamsEntity> paramsEntities) throws IOException {
        String requestBody = "{\n" +
                "  \"data\": {\n" +
                "    \"name\": \"zhang\",\n" +
                "    \"email\": \"zhang@163.com\",\n" +
                "    \"phone\": \"15674840501\",\n" +
                "    \"bank_code\": \"BANXICO\",\n" +
                "    \"account_type\": \"clabe\",\n" +
                "    \"bank_account\": \"431226199609023122\",\n" +
                "    \"document_type\": \"RFC\",\n" +
                "    \"account_id\": \""+String.valueOf(DateTimeUtil.getTransactionMillisecondId())+"\",\n" +
                "    \"pay_method\": \"mex\"\n" +
                "  },\n" +
                "  \"amount\": 1000,\n" +
                "  \"token\": \"${token}\",\n" +
                "  \"user_id\": \"${user_id}\"\n" +
                "}";
        requestBody = (String)new BodyParamsBuilder().parseParams(paramsEntities, requestBody);
        logger.info("api withdraw requestBody :" + requestBody);
        String responseBody = RequestUtil.sendRequest(BASE_URL + "/user/withdraw", "POST", requestBody, null);
        logger.info("api withdraw response :" + responseBody);
        return  ParamsSetUtil.setNullParams();
    }


    public static boolean run(int requestNumber) throws InterruptedException, IOException {
        init(requestNumber);
        for (Integer i = 0; i < requestNumber; i++) {
            executorService.execute(()->{
                logger.info("thread：" + Thread.currentThread().getName() + " start run ...");
                try {
                    Thread.sleep(500);
                    List<ParamsEntity> registerParamsEntitys = registerTest();
                    Thread.sleep(500);
                    List<ParamsEntity> loginParamsEntitys = loginTest(registerParamsEntitys);
                    Thread.sleep(500);
                    List<ParamsEntity> rechargeParamsEntitys = rechargeTest(loginParamsEntitys);
                    Thread.sleep(1500);
                    notifyTest(rechargeParamsEntitys);
                    Thread.sleep(1500);
                    betTest(loginParamsEntitys);
//                    Thread.sleep(1500);
//                    withdraw(loginParamsEntitys);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("thread：" + Thread.currentThread().getName() + " start end ...");
                CurrencyTestCase.countDownLatch.countDown();
            });
        }
        CurrencyTestCase.countDownLatch.await();
        CurrencyTestCase.close();
        return true;
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        run(1);

    }

}
