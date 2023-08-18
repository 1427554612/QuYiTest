package com.zhangjun.quyi.api_auto_test.api_core.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.api_auto_test.api_core.components.asserts.impl.AssertCaseImpl;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGettingFactory;
import com.zhangjun.quyi.api_auto_test.api_core.components.set.DivResponseParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.set.impl.DivResponseParamsSettingImpl;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiAssertEntity;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.service.impl.ApiAutoTestServiceImpl;
import com.zhangjun.quyi.api_auto_test.util.JavaJaninoUtil;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.service_base.websocket.WebSocketServer;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public static void runApi(List<ApiTestCaseEntity> caseList,String configId) throws Exception {
        // 1、循环遍历传递过来的所有用例
        List<ApiTestCaseEntity> skipCaseList = new ArrayList<>();         // 跳过执行的用例列表
        for (ApiTestCaseEntity apiTestCaseEntity : caseList) {

            // 1.1、过滤所有不执行的用例
            if (apiTestCaseEntity.getIsRun().equals("否") || apiTestCaseEntity.getIsRun().equals("N")){
                skipCaseList.add(apiTestCaseEntity);
                continue;
            }
            System.out.println("前置脚本如下：" + apiTestCaseEntity.getBeforeScript());
            // 1.2、处理前置脚本
            if (null!=apiTestCaseEntity.getBeforeScript()){
                Object o = JavaJaninoUtil.runScript(apiTestCaseEntity.getBeforeScript());
                logger.debug("前置搅拌执行结果：" + o);
            }

            // 1.3、获取参数化,替换原本api对象

            if (ParamsGetting.ifNeedGetParams(apiTestCaseEntity)){
                List<ParamsGetting> paramsGettings = ParamsGettingFactory.buildGettingObj(apiTestCaseEntity);
                logger.debug("参数处理器数量：" + paramsGettings.size());
                for (ParamsGetting paramsGetting : paramsGettings) {
                    apiTestCaseEntity = paramsGetting.getParams(DivResponseParamsSetting.ApiParamsEntitys,apiTestCaseEntity);
                }
            }
            System.out.println("替换后的数据：" + apiTestCaseEntity);

            // 1.4、执行接口、获得请求和响应
            Object[] requestAndResponse = RequestUtil.sendingRequest("http://localhost:8000"+apiTestCaseEntity.getApiPath(),
                    apiTestCaseEntity.getRequestMethod(),
                    null ==apiTestCaseEntity.getRequestBody() ? null : apiTestCaseEntity.getRequestBody().toString() ,
                    JsonUtil.objectMapper.readValue(
                            apiTestCaseEntity.getRequestHeaders().toString(), Map.class));
            int code = ((Response) requestAndResponse[1]).code();
            Headers headers = ((Response) requestAndResponse[1]).headers();
            String body = ((Response) requestAndResponse[1]).body().string();
            logger.debug("response = " + body);

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
                logger.debug("需要配置多少个参数？ " + paramsNodeTree.size());
                for (int i = 0; i < paramsNodeTree.size(); i++) {
                    ApiParamsEntity apiParamsEntity = new ApiParamsEntity();
                    apiParamsEntity.setCaseName(apiTestCaseEntity.getCaseName());
                    apiParamsEntity.setParamName(paramsNodeTree.get(i).get("paramName").asText());
                    apiParamsEntity.setParamFrom(paramsNodeTree.get(i).get("paramFrom").asText());
                    apiParamsEntity.setParamsEq(paramsNodeTree.get(i).get("paramsEq").asText());
                    // 调用设置参数
                    divResponseParamsSetting.setParams(body,headers,apiTestCaseEntity.getRequestBody(),apiTestCaseEntity.getRequestHeaders(),apiParamsEntity);
                }
            }
            for (ApiParamsEntity apiParamsEntity : DivResponseParamsSetting.ApiParamsEntitys) {
                logger.debug("参数列表数据：" + apiParamsEntity);
            }

            // 1.2、处理后置
            if (null!=apiTestCaseEntity.getBeforeScript()){
                Object o = JavaJaninoUtil.runScript(apiTestCaseEntity.getBeforeScript());
                logger.debug("后置搅拌执行结果：" + o);
            }


            // 推送消息
            WebSocketServer.sendInfo(JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity), HttpConstant.CONNECTION_SID);

        }

        // 最终阶段：销毁对象&资源
        destroys();

    }

    /**
     * 销毁
     */
    private static void destroys(){
        // 销毁参数获取对象处理类
        ParamsGettingFactory.paramsGettings.clear();
    }


    public static void main(String[] args) throws JsonProcessingException {
        String params  = "[{\\n \\\"caseName\\\": \\\"built\\\",\\n \\\"paramName\\\": \\\"configName\\\",\\n \\\"paramFrom\\\": \\\"responseBody\\\",\\n \\\"paramsEq\\\": \\\"configName\\\\\\\":\\\\\\\"(.* ? )\\\\\\\",\\\"\\n}, {\\n \\\"caseName\\\": \\\"built\\\",\\n \\\"paramName\\\": \\\"configType\\\",\\n \\\"paramFrom\\\": \\\"responseBody\\\",\\n \\\"paramsEq\\\": \\\"configType\\\\\\\":\\\\\\\"(.* ? )\\\\\\\",\\\"\\n}]";
        System.out.println(JsonUtil.objectMapper.readTree(params));
    }
}
