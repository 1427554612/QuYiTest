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

    /**
     * 结果id查询出所有结果详情
     * @param resultId：结果id
     * @param sort：排序方式 1、正序、2、倒叙
     * @return
     */
    List<TestResultInfo> findResultInfoList(String resultId, Integer sort);
}
