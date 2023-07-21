package com.zhangjun.quyi.api_auto_test.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.utils.ResultModel;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ApiAutoTestService {

    /**
     * 执行列表中所有的用例
     * @param caseList
     */
    void runCase(List<ApiTestCaseEntity> caseList,String configId) throws Exception;




    /**
     * 获取所有用例
     * @return
     */
    List<ApiTestCaseEntity> selectAllCase() throws IOException;



    /**
     * 上传文件到本地
     * @param request
     * @return
     */
    Map<String,Object> upload(MultipartHttpServletRequest request) throws IOException;


    /**
     * 操作接口测试用例
     * @param testCaseEntity
     */
    void editApiTestCase(List<ApiTestCaseEntity> testCaseEntitys) throws IOException;


}
