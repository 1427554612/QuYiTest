package com.zhangjun.quyi.test_result.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;

import java.util.List;

public interface TestResultInfoService extends IService<TestResultInfo> {

    /**
     * 添加结果详情
     * @param testResultInfo
     * @return
     */
    TestResultInfo saveResultInfo(TestResultInfo testResultInfo);

    /**
     * 通过resultId查询所有执行详情记录
     * @return
     */
    IPage<TestResultInfo> findAllInfoByResultId(String resultId, Integer current, Integer size);
}
