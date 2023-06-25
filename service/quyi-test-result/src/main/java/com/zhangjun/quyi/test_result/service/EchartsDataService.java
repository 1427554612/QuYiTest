package com.zhangjun.quyi.test_result.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.test_result.entity.TestResult;

import java.util.Map;

public interface EchartsDataService extends IService<TestResult> {

    /**
     * 以用例名称分组获取成功率
     */
    Map<String,Object> getCaseSuccessRate() throws JsonProcessingException;


    /**
     * 以平台区分成功总数和失败总数
     * @return
     */
    Map<String, Object> getPlatformSuccessAndErrorNum() throws JsonProcessingException;
}
