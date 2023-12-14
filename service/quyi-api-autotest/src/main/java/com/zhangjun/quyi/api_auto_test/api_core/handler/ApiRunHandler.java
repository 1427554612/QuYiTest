package com.zhangjun.quyi.api_auto_test.api_core.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.api_auto_test.api.TestConfigApi;
import com.zhangjun.quyi.api_auto_test.api.TestResultApi;
import com.zhangjun.quyi.api_auto_test.api_core.components.asserts.impl.AssertCaseImpl;
import com.zhangjun.quyi.api_auto_test.api_core.components.param.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.param.get.ParamsGettingFactory;
import com.zhangjun.quyi.api_auto_test.api_core.components.param.set.ParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.param.set.impl.ApiParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiAssertEntity;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsEnums;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.entity.remoteEntity.TestResult;
import com.zhangjun.quyi.api_auto_test.entity.remoteEntity.TestResultInfo;
import com.zhangjun.quyi.api_auto_test.util.JavaJaninoUtil;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.constans.StrConstant;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.service_base.websocket.WebSocketServer;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import com.zhangjun.quyi.utils.ResultModel;
import okhttp3.Headers;
import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 核心控制器，控制自动化程序执行
 */
public class ApiRunHandler {

    // 默认请求数量
    public static final Integer DEFAULT_REQUEST_NUMBER = 10;


    /**
     * 初始化
     */
    static {
        RequestUtil.setOkhttpClient(ApiRunHandler.DEFAULT_REQUEST_NUMBER,ApiRunHandler.DEFAULT_REQUEST_NUMBER);
        LogStringBuilder.logger = LoggerFactory.getLogger(ApiRunHandler.class);
    }

    /**
     * 初始化
     * @param configId
     */
    private static String init(String configId,TestConfigApi testConfigApi,TestResultApi testResultApi) throws JsonProcessingException {
        ResultModel resultModel = testConfigApi.selectConfigById(configId);
        Object testConfig = resultModel.getData().get("testConfig");
        String s = JsonUtil.objectMapper.writeValueAsString(testConfig);
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(s).get("configData").get("clientUrl");

        // 销毁参数获取对象处理类
        ParamsGettingFactory.paramsGettings.clear();
        // 清空临时结果数据
        testResultApi.clearResultTemp();
        return jsonNode.asText();
    }

    /**
     * 执行方法
     * @param caseList：勾选的执行用例列表
     * @param configId：关联配置id
     */
    public static void runApi(List<ApiTestCaseEntity> caseList, String configId, TestConfigApi testConfigApi, TestResultApi testResultApi) throws Exception {
        // 最终阶段：销毁对象&资源
        String baseUrl = init(configId,testConfigApi,testResultApi);
        LogStringBuilder.addLog(LogStringBuilder.START_TEST);
        LogStringBuilder.addLog("base url:" + baseUrl);
        // 1、循环遍历传递过来的所有用例
        List<ApiTestCaseEntity> skipCaseList = new ArrayList<>();         // 跳过执行的用例列表
        for (ApiTestCaseEntity apiTestCaseEntity : caseList) {
            // 1.1、过滤所有不执行的用例
            if (apiTestCaseEntity.getIsRun().equals("否") || apiTestCaseEntity.getIsRun().equals("N")){
                skipCaseList.add(apiTestCaseEntity);
                LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + apiTestCaseEntity.getCaseName() + StrConstant.SYMBOL_COMMA + LogStringBuilder.SKIP_CASE);
            }
            LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + apiTestCaseEntity.getCaseName() + LogStringBuilder.CASE_START);

            // 异常处理
            try {
                runApiService(apiTestCaseEntity,configId,testConfigApi,testResultApi,baseUrl);
            }catch (Exception e ){
                e.printStackTrace();
                if (apiTestCaseEntity.getIsMainProcessApi().equals("是")){
                    throw new ExceptionEntity(20001,"主流程用例执行异常 ... ");
                }
            }
        }

    }

    /**
     * 开始执行
     * @param configId
     * @param testConfigApi
     * @param testResultApi
     * @throws Exception
     */
    public static void runApiService(ApiTestCaseEntity apiTestCaseEntity, String configId, TestConfigApi testConfigApi, TestResultApi testResultApi,String baseUrl) throws Exception{
        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + apiTestCaseEntity.getCaseName() + LogStringBuilder.CASE_START);
        // 先创建datas和resultInfo对象
        String apiEntityJsonStr = JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity);
        Map<String,Object> datas = JsonUtil.objectMapper.readValue(apiEntityJsonStr, Map.class);

        TestResultInfo testResultInfo = new TestResultInfo();
        testResultInfo.setCaseName(apiTestCaseEntity.getCaseName());
        testResultInfo.setDatas(datas);
        testResultInfo.setRunBeginTime(new Date());
        testResultInfo.setConfigId(configId);

        // 1.2、处理前置脚本
        if (null!=apiTestCaseEntity.getBeforeScript()){
            Object o = JavaJaninoUtil.runScript(apiTestCaseEntity.getBeforeScript());
            LogStringBuilder.addLog(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA +LogStringBuilder.BEFORE_SCRIPT_RUN + o);
        }

        // 1.3、获取参数化,替换原本api对象

        if (ParamsGetting.ifNeedGetParams(apiTestCaseEntity)){
            List<ParamsGetting> paramsGettings = ParamsGettingFactory.buildGettingObj(apiTestCaseEntity);
            LogStringBuilder.addLog(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA + LogStringBuilder.PARAMS_HANDLE_NUMBER + paramsGettings.size());
            for (ParamsGetting paramsGetting : paramsGettings) {
                apiTestCaseEntity = paramsGetting.getParams(ParamsSetting.apiParamsEntitys,apiTestCaseEntity);
            }
        }

        // 1.4、执行接口、获得请求和响应
        Long startTime = System.currentTimeMillis();
        Object[] requestAndResponse = RequestUtil.sendingRequest(baseUrl+apiTestCaseEntity.getApiPath(),
                apiTestCaseEntity.getRequestMethod(),
                null ==apiTestCaseEntity.getRequestBody() ? null : apiTestCaseEntity.getRequestBody().toString() ,
                JsonUtil.objectMapper.readValue(
                        apiTestCaseEntity.getRequestHeaders().toString(), Map.class));
        Long endTime = System.currentTimeMillis();
        int code = ((Response) requestAndResponse[1]).code();
        Headers headers = ((Response) requestAndResponse[1]).headers();
        String body = ((Response) requestAndResponse[1]).body().string();
        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA + LogStringBuilder.RESPONSE_BODY_DATA + body.replaceAll("\n","").replaceAll(" ",""));

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
            ApiParamsSetting apiResponseParamsSetting = new ApiParamsSetting();
            Object paramList = apiTestCaseEntity.getParamList();
            JsonNode paramsNodeTree = JsonUtil.objectMapper.readTree(paramList.toString());
            for (int i = 0; i < paramsNodeTree.size(); i++) {
                ApiParamsEntity apiParamsEntity = new ApiParamsEntity();
                apiParamsEntity.setCaseName(apiTestCaseEntity.getCaseName());
                apiParamsEntity.setParamName(paramsNodeTree.get(i).get("paramName").asText());
                apiParamsEntity.setParamFrom(paramsNodeTree.get(i).get("paramFrom").asText());
                apiParamsEntity.setParamEq(paramsNodeTree.get(i).get("paramsEq").asText());
                // 调用设置参数
                apiResponseParamsSetting.setParams(body,headers,apiTestCaseEntity.getRequestBody(),apiTestCaseEntity.getRequestHeaders(),apiParamsEntity);
                LogStringBuilder.addLog(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA + LogStringBuilder.SET_PARAMS_DATA + apiParamsEntity);
            }
        }

        // 1.2、处理后置
        if (null!=apiTestCaseEntity.getBeforeScript()){
            Object o = JavaJaninoUtil.runScript(apiTestCaseEntity.getBeforeScript());
            LogStringBuilder.addLog(LogStringBuilder.CASE_NAME +apiTestCaseEntity.getCaseName() +StrConstant.SYMBOL_COMMA +LogStringBuilder.AFTER_SCRIPT_RUN + o);
        }

        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + apiTestCaseEntity.getCaseName() + LogStringBuilder.CASE_END);

        testResultInfo.setRunEndTime(new Date());
        testResultInfo.setRunResult(flag);
        testResultInfo.setRunTime(endTime-startTime);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",code);
        resultMap.put(ParamsEnums.ParamsFromEnum.RESPONSE_HEADER.value,headers);
        resultMap.put(ParamsEnums.ParamsFromEnum.RESPONSE_BODY.value, JsonUtil.objectMapper.readValue(body,Map.class));
        testResultInfo.setResultData(resultMap);
        testResultInfo.setResultLog(LogStringBuilder.getLog());
        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + apiTestCaseEntity + "end case...");

        // 清空日志添加器
        LogStringBuilder.setLength(0);

        // 推送消息
        WebSocketServer.sendInfo(JsonUtil.objectMapper.writeValueAsString(testResultInfo), HttpConstant.CONNECTION_SID);

        // 结果写入到数据库
        addOrUpdateResult(testResultInfo,testResultApi,testConfigApi);


    }


    /**
     * 添加或者修改结果
     */
    private static void addOrUpdateResult(TestResultInfo testResultInfo,TestResultApi testResultApi,TestConfigApi testConfigApi) throws JsonProcessingException {
        TestResult testResult = new TestResult();
        testResult.setCaseName(testResultInfo.getCaseName());
        testResult.setResultType("api");
        testResult.setResultData(testResultInfo.getResultData());
        testResult.setDatas(testResultInfo.getDatas());
        testResult.setResultLog(testResultInfo.getResultLog());
        testResult.setLastRunResult(testResultInfo.isRunResult());
        testResult.setLastRunDate(testResultInfo.getRunBeginTime());
        testResult.setLastRunTime(testResultInfo.getRunTime());

        // 获取最后执行的配置
        ResultModel testConfigModel = testConfigApi.selectConfigById(testResultInfo.getConfigId());
        Object testConfig = testConfigModel.getData().get(HttpConstant.API_STR_TEST_CONFIG);
        String configName = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(testConfig)).get(HttpConstant.API_STR_CONFIG_NAME).asText();
        testResult.setConfigName(configName);

        // 查询数据库中是否有这个数据
        Map<String, Object> resultData = testResultApi.findResultByCaseName(testResult.getCaseName()).getData();
        if (resultData.get(HttpConstant.RESPONSE_STR_DATA) == null || resultData.get(HttpConstant.RESPONSE_STR_DATA).equals("null")){
            testResult.setRunNum(1);
            testResult.setRunErrorNum(testResultInfo.isRunResult() ? 0 : 1);
            testResult.setRunSuccessNum( !testResultInfo.isRunResult()  ? 0 : 1);
            testResult.setRunSuccessRate(testResultInfo.isRunResult()  ? 100.00 : 0.00);
            // 添加testResult
            testResult.setLastRunResult(testResultInfo.isRunResult());
            ResultModel resultModel = testResultApi.saveResult(testResultInfo.getConfigId(), testResult);
            // 获取添加后 result对象的 resultId
            if(resultModel.getCode() == 20000){
                ResultModel findResultModel = testResultApi.findResultByCaseName(testResult.getCaseName());
                Object data = findResultModel.getData().get(HttpConstant.RESPONSE_STR_DATA);
                String resultId = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(data)).get(HttpConstant.API_STR_RESULT_ID).asText();
                // 添加结果详情
                testResultInfo.setResultId(resultId);
                testResultApi.saveResultInfo(testResultInfo);
            }
        }
        else {
            Object data = resultData.get(HttpConstant.RESPONSE_STR_DATA);
            String resultId = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(data)).get(HttpConstant.API_STR_RESULT_ID).asText();
            testResult.setResultId(resultId);
            Map<String,Object> testResultMap = new HashMap<>();
            testResultMap.put("testResult",testResult);
            testResultMap.put("testResultInfo",testResultInfo);
            testResultApi.updateResult(testResultMap);
        }

    }

}
