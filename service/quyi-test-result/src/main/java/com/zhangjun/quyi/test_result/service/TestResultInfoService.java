package com.zhangjun.quyi.test_result.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;

public interface TestResultInfoService extends IService<TestResultInfo> {

    /**
     * 添加结果详情
     * @param testResultInfo
     * @return
     */
    TestResultInfo saveResultInfo(TestResultInfo testResultInfo);
}
