package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.mapper.TestResultInfoServiceMapper;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import org.springframework.stereotype.Service;

@Service
public class TestResultInfoServiceImpl extends ServiceImpl<TestResultInfoServiceMapper, TestResultInfo> implements TestResultInfoService {

}
