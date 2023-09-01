package com.zhangjun.quyi.api_auto_test.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.api_auto_test.api.TestConfigApi;
import com.zhangjun.quyi.api_auto_test.api.TestResultApi;
import com.zhangjun.quyi.api_auto_test.api_core.handler.ApiRunHandler;
import com.zhangjun.quyi.api_auto_test.controller.ApiAutoTestController;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.entity.remoteEntity.TestConfigInfo;
import com.zhangjun.quyi.api_auto_test.service.ApiAutoTestService;
import com.zhangjun.quyi.api_auto_test.util.EasyExcelUtil;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.constans.StrConstant;
import com.zhangjun.quyi.utils.JsonUtil;

import com.zhangjun.quyi.utils.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
        TestConfigInfo testConfigInfo = new TestConfigInfo();
        testConfigInfo.setConfigId(configId);
        ResultModel resultModel = testConfigApi.saveTestConfigInfo(testConfigInfo);

        ResultModel apiCaseResultModel = apiAutoTestController.selectAllCase();
        List<ApiTestCaseEntity> allCaseList = (List<ApiTestCaseEntity>)apiCaseResultModel.getData().get(HttpConstant.RESPONSE_STR_LIST);
        // 获取基础配置

        // 执行用例
        ApiRunHandler.runApi(caseList,configId,testConfigApi,testResultApi);
    }


    /**
     * 获取所有用例
     * @return
     */
    @Override
    @Cacheable(value = "TestCase",key = "'list'",cacheManager = "cacheManager3Minute")
    public List<ApiTestCaseEntity> selectAllCase() throws IOException {
        ResultModel resultModel = testConfigApi.getConfigPath();
        String configPath = (String)resultModel.getData().get("configPath");
        logger.info("配置文件path：" + configPath);
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(new File(configPath));
        logger.info("配置信息如下：" +jsonNode);
        String excelPath  = jsonNode.get(HttpConstant.API_STR_EXCEL_PATH).textValue();
        logger.info("用例文件path：" + excelPath);

        List<ApiTestCaseEntity> apiTestCaseEntities = EasyExcelUtil.readExcel(new ApiTestCaseEntity(), excelPath);
        return apiTestCaseEntities;
    }


    /**
     * 编辑测试用例
     * @param testCaseEntitys
     */
    @Override
    @CacheEvict(value = "TestCase",key = "'list'")
    public void editApiTestCase(List<ApiTestCaseEntity> testCaseEntitys) throws IOException {
        for (ApiTestCaseEntity testCaseEntity : testCaseEntitys) {
            System.out.println(testCaseEntity);
        }
        ResultModel resultModel = testConfigApi.getConfigPath();
        String configPath = (String)resultModel.getData().get("configPath");
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(new File(configPath));
        String excelPath  = jsonNode.get(HttpConstant.API_STR_EXCEL_PATH).textValue();
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

}
