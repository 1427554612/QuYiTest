package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.mapper.TestResultInfoServiceMapper;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import org.springframework.stereotype.Service;

@Service
public class TestResultInfoServiceImpl extends ServiceImpl<TestResultInfoServiceMapper, TestResultInfo> implements TestResultInfoService {

    /**
     * 添加结果详情
     * @param testResultInfo
     * @return
     */
    @Override
    public TestResultInfo saveResultInfo(TestResultInfo testResultInfo) {
        boolean flag = this.save(testResultInfo);
        if (!flag) throw new ExceptionEntity(20001,"添加结果详情错误");
        return testResultInfo;
    }
}
