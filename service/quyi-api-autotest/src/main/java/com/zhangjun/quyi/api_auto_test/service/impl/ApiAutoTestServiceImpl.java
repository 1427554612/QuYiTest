package com.zhangjun.quyi.api_auto_test.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.api_auto_test.api.TestConfigApi;
import com.zhangjun.quyi.api_auto_test.api.TestResultApi;
import com.zhangjun.quyi.api_auto_test.controller.ApiAutoTestController;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.entity.TestResult;
import com.zhangjun.quyi.api_auto_test.entity.TestResultInfo;
import com.zhangjun.quyi.api_auto_test.entity.dto.TestResultDto;
import com.zhangjun.quyi.api_auto_test.service.ApiAutoTestService;
import com.zhangjun.quyi.api_auto_test.util.EasyExcelUtil;
import com.zhangjun.quyi.api_auto_test.util.PythonScriptUtil;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.constans.StrConstant;
import com.zhangjun.quyi.service_base.websocket.WebSocketServer;
import com.zhangjun.quyi.utils.DateTimeUtil;
import com.zhangjun.quyi.utils.JsonUtil;

import com.zhangjun.quyi.utils.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;

import java.util.*;

@Service
public class ApiAutoTestServiceImpl implements ApiAutoTestService {

    private static Logger logger  = LoggerFactory.getLogger(ApiAutoTestServiceImpl.class);

    @Autowired
    private TestConfigApi testConfigApi;

    @Autowired
    private ApiAutoTestController apiAutoTestController;

    @Autowired
    private TestResultApi testResultApi;


    /**
     * 批量执行
     * @param caseList
     */
    @Override
    public void runCase(List<ApiTestCaseEntity> caseList,String configId) throws Exception {
        // 更新配置文件
        ResultModel configResultModel = testConfigApi.getConfigPath();
        updateFile(configId,configResultModel);
        String pythonProjectPath = (String)configResultModel.getData().get("pythonProjectPath");
        String reportPath = (String)configResultModel.getData().get(StrConstant.REPORT_PATH);

        ResultModel apiCaseResultModel = apiAutoTestController.selectAllCase();
        List<ApiTestCaseEntity> allCaseList = (List<ApiTestCaseEntity>)apiCaseResultModel.getData().get(HttpConstant.RESPONSE_STR_LIST);
        // 执行所有
        if (allCaseList.size() == caseList.size()){
            PythonScriptUtil
                    .execute("pytest -vs --html="+reportPath+"/report.html --capture=sys -p no:warnings",pythonProjectPath);
        }else
            PythonScriptUtil
                    .execute("pytest -vs --html="+reportPath+"/report.html --capture=sys -p no:warnings",pythonProjectPath);
        Thread.sleep(5000);
        this.addOrUpdateResult(configId,caseList);   // 添加或者更新结果
    }



    /**
     * 获取所有用例
     * @return
     */
    @Override
    public List<ApiTestCaseEntity> selectAllCase() throws IOException {
        ResultModel resultModel = testConfigApi.getConfigPath();
        String configPath = (String)resultModel.getData().get("configPath");
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(new File(configPath));
        String excelPath  = jsonNode.get("excelPath").textValue();
        List<ApiTestCaseEntity> apiTestCaseEntities = EasyExcelUtil.readExcel(new ApiTestCaseEntity(), excelPath);
        return apiTestCaseEntities;
    }



    /**
     * 编辑测试用例
     * @param testCaseEntitys
     */
    @Override
    public void editApiTestCase(List<ApiTestCaseEntity> testCaseEntitys) throws IOException {
        ResultModel resultModel = testConfigApi.getConfigPath();
        String configPath = (String)resultModel.getData().get("configPath");
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(new File(configPath));
        String excelPath  = jsonNode.get("excelPath").textValue();
        EasyExcelUtil.exportExcel(ApiTestCaseEntity.class,testCaseEntitys,excelPath);
    }

    /**
     * 上传文件到本地
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> upload(MultipartHttpServletRequest request) throws IOException {
        MultipartFile file = request.getFile(StrConstant.FILE_NAME);
        ResultModel resultModel = testConfigApi.getConfigPath();
        String configPath = (String)resultModel.getData().get(StrConstant.CONFIG_PATH);
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(new File(configPath));
        String parentPath = new File(jsonNode.get(StrConstant.EXCEL_PATH).textValue()).getParent();
        String fileName = file.getOriginalFilename();
        file.transferTo(new File(parentPath,fileName));
        Map<String,Object> map = new HashMap<>();
        map.put("filePath",parentPath + fileName);
        return map;
    }



    /**
     * 更新配置文件
     * @param configId
     * @throws IOException
     */
    private void updateFile(String configId,ResultModel resultModel) throws IOException {
        String configPath = (String)resultModel.getData().get(StrConstant.CONFIG_PATH);
        // 写入配置文件
        File file = new File(configPath);
        Map<String,Object> configMap = (Map<String,Object>)testConfigApi.selectConfigById(configId).getData().get("testConfig");
        Map<String,Object> configData = (Map<String,Object>)configMap.get("configData");
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(configData));
        JsonUtil.objectMapper.writeValue(file,jsonNode);

    }

    /**
     * 添加或者修改结果
     * @throws IOException
     */
    private void addOrUpdateResult(String configId,List<ApiTestCaseEntity> caseList) throws Exception {
        // 结果写入库
        ResultModel resultModel = testConfigApi.getConfigPath();
        String apiRunTimePath = (String)resultModel.getData().get("apiRunTimePath");
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(apiRunTimePath)));
        // 运行文件的jsonNode对象
        String json = null;
        List<JsonNode> jsonNodes = new ArrayList<>();
        while ((json =bf.readLine())!= null){
            jsonNodes.add(JsonUtil.objectMapper.readTree(json));
        }
        try {
            for (int i = 0;i<caseList.size();++i){
                TestResultDto testResultDto = new TestResultDto();
                testResultDto.setCase_name(caseList.get(i).getCaseName());
                testResultDto.setCase_type("api");
                if (jsonNodes.get(i).get("case_name").asText().equals(caseList.get(i).getCaseName())){
                    boolean run_result = Boolean.valueOf(jsonNodes.get(i).get("run_result").asText());
                    Date beginTime = DateTimeUtil.stringForDate(jsonNodes.get(i).get("run_begin_time").asText());
                    Date endTime = DateTimeUtil.stringForDate(jsonNodes.get(i).get("run_end_time").asText());
                    int run_time = jsonNodes.get(i).get("run_time").asInt();
                    TestResultInfo testResultInfo = new TestResultInfo();
                    testResultInfo.setRun_result(run_result);
                    testResultInfo.setRun_begin_time(beginTime);
                    testResultInfo.setRun_end_time(endTime);
                    testResultInfo.setRun_time(run_time);
                    testResultDto.getTestResultInfoList().add(testResultInfo);
                    testResultDto.setLast_run_date(testResultInfo.getRun_begin_time());
                    testResultDto.setLast_run_time(run_time);
                    testResultDto.setCase_title(jsonNodes.get(i).get("case_title").asText());
                    testResultDto.setResponse_data(JsonUtil.objectMapper.readValue(jsonNodes.get(i).get("response_data").toString(),Map.class));
                    logger.info("testResultDto = " + testResultDto);
                    String data = JsonUtil.objectMapper.writeValueAsString(testResultApi.findResultByCaseName(caseList.get(i).getCaseName()).getData().get("data"));
                    JsonNode jsonNode = JsonUtil.objectMapper.readTree(data);
                    if (null == data || "null".equals(data))
                    {
                        logger.info("执行 saveResult method");
                        testResultApi.saveResult(configId,testResultDto);
                    }
                    else {
                        logger.info("执行 updateResult method");
                        testResultDto.setResult_id(jsonNode.get("result_id").asText());
                        testResultApi.updateResult(testResultDto,configId);
                    };
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            bf.close();
            logger.info("apiRunTimePath io 已关闭");
        }
    }




}
