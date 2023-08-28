package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.TestResultTempInfo;
import com.zhangjun.quyi.test_result.mapper.TestResultInfoServiceMapper;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import com.zhangjun.quyi.test_result.service.TestResultTempInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestResultInfoServiceImpl extends ServiceImpl<TestResultInfoServiceMapper, TestResultInfo> implements TestResultInfoService {

    @Autowired
    private TestResultTempInfoService testResultTempInfoService;
    /**
     * 添加结果详情
     * @param testResultInfo
     * @return
     */
    @Override
    public TestResultInfo saveResultInfo(TestResultInfo testResultInfo) {
        boolean flag = this.save(testResultInfo);
        if (!flag) throw new ExceptionEntity(20001,"添加结果详情错误");
        // 查询这条插入的数据
        QueryWrapper<TestResultInfo> testResultInfoQueryWrapper = new QueryWrapper<>();
        testResultInfoQueryWrapper.eq("case_name",testResultInfo.getCaseName());
        testResultInfoQueryWrapper.orderByDesc("run_end_time").last("limit 1");
        TestResultInfo selectResultInfo = this.getOne(testResultInfoQueryWrapper);
        System.out.println("查询出来的详情对象：" + selectResultInfo);
        // 插入临时表
        TestResultTempInfo testResultTempInfo = new TestResultTempInfo();
        BeanUtils.copyProperties(selectResultInfo,testResultTempInfo);
        testResultTempInfoService.save(testResultTempInfo);
        return selectResultInfo;
    }

    /**
     * 通过resultId查询所有执行详情记录
     * @return
     */
    @Override
    @Cacheable(value = "test-result",key = "#resultId",cacheManager = "cacheManager1Minute")
    public List<TestResultInfo> findAllInfoByResultId(String resultId) {
        QueryWrapper<TestResultInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("result_id",resultId);
        return this.list(queryWrapper);
    }
}
