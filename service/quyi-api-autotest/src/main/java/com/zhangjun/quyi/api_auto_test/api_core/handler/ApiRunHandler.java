package com.zhangjun.quyi.api_auto_test.api_core.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.api_auto_test.api.TestConfigApi;
import com.zhangjun.quyi.api_auto_test.api_core.components.asserts.impl.AssertCaseImpl;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGettingFactory;
import com.zhangjun.quyi.api_auto_test.api_core.components.set.DivResponseParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.set.impl.DivResponseParamsSettingImpl;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiAssertEntity;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.entity.remoteEntity.TestResult;
import com.zhangjun.quyi.api_auto_test.entity.remoteEntity.TestResultInfo;
import com.zhangjun.quyi.api_auto_test.service.impl.ApiAutoTestServiceImpl;
import com.zhangjun.quyi.api_auto_test.util.JavaJaninoUtil;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.constans.StrConstant;
import com.zhangjun.quyi.service_base.websocket.WebSocketServer;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import com.zhangjun.quyi.utils.ResultModel;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 核心控制器，控制自动化程序执行
 */
public class ApiRunHandler {

    // 默认请求数量
    public static final Integer DEFAULT_REQUEST_NUMBER = 10;
    private static Logger logger  = LoggerFactory.getLogger(ApiRunHandler.class);


    /**
     * 1、初始化请求池
     */
    static {
        RequestUtil.setOkhttpClient(ApiRunHandler.DEFAULT_REQUEST_NUMBER,ApiRunHandler.DEFAULT_REQUEST_NUMBER);
    }

    /**
     * 执行方法
     * @param caseList：勾选的执行用例列表
     * @param configId：关联配置id
     */
    public static void runApi(List<ApiTestCaseEntity> caseList,String configId,TestConfigApi testConfigApi) throws Exception {
        // 最终阶段：销毁对象&资源
        String baseUrl = init(configId,testConfigApi);
        logAdd(LogStringBuilder.START_TEST);
        logAdd("base url:" + baseUrl);
        // 1、循环遍历传递过来的所有用例
        List<ApiTestCaseEntity> skipCaseList = new ArrayList<>();         // 跳过执行的用例列表
        for (ApiTestCaseEntity apiTestCaseEntity : caseList) {
            // 1.1、过滤所有不执行的用例
            if (apiTestCaseEntity.getIsRun().equals("否") || apiTestCaseEntity.getIsRun().equals("N")){
                skipCaseList.add(apiTestCaseEntity);
                logAdd(LogStringBuilder.CASE_NAME + apiTestCaseEntity.getCaseName() + StrConstant.SYMBOL_COMMA + LogStringBuilder.SKIP_CASE);
                continue;
            }
            logAdd(LogStringBuilder.CASE_NAME + apiTestCaseEntity + "start case...");
            // 先创建datas和resultInfo对象
            String apiEntityJsonStr = JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity);
            Map<String,Object> datas = JsonUtil.objectMapper.readValue(apiEntityJsonStr, Map.class);

            TestResultInfo testResultInfo = new TestResultInfo();
            testResultInfo.setDatas(datas);
            testResultInfo.setRunBeginTime(new Date());
            testResultInfo.setPlatformId(configId);
            Long startTime = System.currentTimeMillis();

            // 1.2、处理前置脚本
            if (null!=apiTestCaseEntity.getBeforeScript()){
                Object o = JavaJaninoUtil.runScript(apiTestCaseEntity.getBeforeScript());
                logAdd(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA +LogStringBuilder.BEFORE_SCRIPT_RUN + o);
            }

            // 1.3、获取参数化,替换原本api对象

            if (ParamsGetting.ifNeedGetParams(apiTestCaseEntity)){
                List<ParamsGetting> paramsGettings = ParamsGettingFactory.buildGettingObj(apiTestCaseEntity);
                logAdd(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA + LogStringBuilder.PARAMS_HANDLE_NUMBER + paramsGettings.size());
                for (ParamsGetting paramsGetting : paramsGettings) {
                    apiTestCaseEntity = paramsGetting.getParams(DivResponseParamsSetting.ApiParamsEntitys,apiTestCaseEntity);
                }
                logAdd(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA +LogStringBuilder.REPLACE_PARAMS_DATA + apiTestCaseEntity);
            }


            // 1.4、执行接口、获得请求和响应
            Object[] requestAndResponse = RequestUtil.sendingRequest(baseUrl+apiTestCaseEntity.getApiPath(),
                    apiTestCaseEntity.getRequestMethod(),
                    null ==apiTestCaseEntity.getRequestBody() ? null : apiTestCaseEntity.getRequestBody().toString() ,
                    JsonUtil.objectMapper.readValue(
                            apiTestCaseEntity.getRequestHeaders().toString(), Map.class));
            int code = ((Response) requestAndResponse[1]).code();
            Headers headers = ((Response) requestAndResponse[1]).headers();
            String body = ((Response) requestAndResponse[1]).body().string();
            logAdd(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA + LogStringBuilder.RESPONSE_BODY_DATA + body.replaceAll("\n","").replaceAll(" ",""));

            // 1.5、判断断言
            Object assertMap = apiTestCaseEntity.getAssertMap();
            JsonNode assertJsonNode = JsonUtil.objectMapper.readTree(assertMap.toString());
            ApiAssertEntity apiAssertEntity = new ApiAssertEntity();
            apiAssertEntity.setKey(assertJsonNode.get("key").textValue());
            apiAssertEntity.setFrom(assertJsonNode.get("from").textValue());
            apiAssertEntity.setType(assertJsonNode.get("type").asText());
            apiAssertEntity.setExpectValue(assertJsonNode.get("expectValue").asText());
            AssertCaseImpl assertCase = new AssertCaseImpl();
            boolean flag = assertCase.baseAssert(apiAssertEntity, code,headers,body);

            // 1.6、设置参数
            if (apiTestCaseEntity.getIsParams().equals("是") || apiTestCaseEntity.getIsParams().equals("Y")){
                DivResponseParamsSettingImpl divResponseParamsSetting = new DivResponseParamsSettingImpl();
                Object paramList = apiTestCaseEntity.getParamList();
                JsonNode paramsNodeTree = JsonUtil.objectMapper.readTree(paramList.toString());
                for (int i = 0; i < paramsNodeTree.size(); i++) {
                    ApiParamsEntity apiParamsEntity = new ApiParamsEntity();
                    apiParamsEntity.setCaseName(apiTestCaseEntity.getCaseName());
                    apiParamsEntity.setParamName(paramsNodeTree.get(i).get("paramName").asText());
                    apiParamsEntity.setParamFrom(paramsNodeTree.get(i).get("paramFrom").asText());
                    apiParamsEntity.setParamsEq(paramsNodeTree.get(i).get("paramsEq").asText());
                    // 调用设置参数
                    divResponseParamsSetting.setParams(body,headers,apiTestCaseEntity.getRequestBody(),apiTestCaseEntity.getRequestHeaders(),apiParamsEntity);
                    logAdd(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA + LogStringBuilder.SET_PARAMS_DATA + apiParamsEntity);
                }
            }

            // 1.2、处理后置
            if (null!=apiTestCaseEntity.getBeforeScript()){
                Object o = JavaJaninoUtil.runScript(apiTestCaseEntity.getBeforeScript());
                logAdd(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA +LogStringBuilder.AFTER_SCRIPT_RUN + o);
            }

            testResultInfo.setRunEndTime(new Date());
            testResultInfo.setRunResult(flag);
            testResultInfo.setRunTime((System.currentTimeMillis() - startTime));
            testResultInfo.setResultData(JsonUtil.objectMapper.readValue(body,Map.class));
            testResultInfo.setResultLog(LogStringBuilder.getLog());

            logAdd(LogStringBuilder.CASE_NAME + apiTestCaseEntity + "end case...");

            System.out.println("final logs = " + testResultInfo.getResultLog());

            LogStringBuilder.setLength(0);

            // 推送消息
            WebSocketServer.sendInfo(JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity), HttpConstant.CONNECTION_SID);

        }


    }

    /**
     * 添加或者修改结果
     */
    private static void addOrUpdateResult(String configId,ApiTestCaseEntity apiTestCaseEntity){
        TestResult testResult = new TestResult();

    }


    /**
     * 日志添加方法
     * @param log
     */
    public static void logAdd(String log){
        LogStringBuilder.addLog(log);
        logger.debug(log);
    }

    /**
     * 销毁
     */
    private static void destroys(){
        // 销毁参数获取对象处理类
        ParamsGettingFactory.paramsGettings.clear();
    }

    /**
     * 初始化
     * @param configId
     */
    private static String init(String configId,TestConfigApi testConfigApi) throws JsonProcessingException {
        ResultModel resultModel = testConfigApi.selectConfigById(configId);
        Object testConfig = resultModel.getData().get("testConfig");
        String s = JsonUtil.objectMapper.writeValueAsString(testConfig);
        System.out.println(s);
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(s).get("configData").get("clientUrl");
        System.out.println(jsonNode.asText());

        // 销毁参数获取对象处理类
        ParamsGettingFactory.paramsGettings.clear();
        return jsonNode.asText();
    }


    public static void main(String[] args) throws JsonProcessingException {
        String params  = "[{\\n \\\"caseName\\\": \\\"built\\\",\\n \\\"paramName\\\": \\\"configName\\\",\\n \\\"paramFrom\\\": \\\"responseBody\\\",\\n \\\"paramsEq\\\": \\\"configName\\\\\\\":\\\\\\\"(.* ? )\\\\\\\",\\\"\\n}, {\\n \\\"caseName\\\": \\\"built\\\",\\n \\\"paramName\\\": \\\"configType\\\",\\n \\\"paramFrom\\\": \\\"responseBody\\\",\\n \\\"paramsEq\\\": \\\"configType\\\\\\\":\\\\\\\"(.* ? )\\\\\\\",\\\"\\n}]";
        System.out.println(JsonUtil.objectMapper.readTree(params));
    }
}
