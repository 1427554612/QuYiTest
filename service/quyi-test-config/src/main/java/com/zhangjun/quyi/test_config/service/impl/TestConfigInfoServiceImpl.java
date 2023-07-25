package com.zhangjun.quyi.test_config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.constans.SqlConstant;
import com.zhangjun.quyi.test_config.entity.TestConfig;
import com.zhangjun.quyi.test_config.entity.TestConfigInfo;
import com.zhangjun.quyi.test_config.mapper.TestConfigInfoMapper;
import com.zhangjun.quyi.test_config.service.TestConfigInfoService;
import com.zhangjun.quyi.test_config.service.TestConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestConfigInfoServiceImpl  extends ServiceImpl<TestConfigInfoMapper, TestConfigInfo>  implements TestConfigInfoService {

    private Logger logger  = LoggerFactory.getLogger(TestConfigServiceImpl.class);

    @Autowired
    private TestConfigService testConfigService;

    /**
     * 保存配置详情信息
     * @param testConfigInfo
     * @return
     */
    @Override
    public TestConfigInfo saveTestConfigInfo(TestConfigInfo testConfigInfo) {
        String configId = testConfigInfo.getConfigId();
        TestConfig testConfig = testConfigService.getById(configId);
        System.out.println(testConfig);
        testConfigInfo.setConfigData(testConfig.getConfigData());
        this.save(testConfigInfo);
        QueryWrapper<TestConfigInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(HttpConstant.API_STR_CREATE_TIME);
        queryWrapper.last(SqlConstant.SQL_LIMIT_1);
        return this.getOne(queryWrapper);
    }

    /**
     * 查询最新一条配置详情
     * @return
     */
    @Override
    public TestConfigInfo findTestConfigInfo() {
        QueryWrapper<TestConfigInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(HttpConstant.API_STR_CREATE_TIME);
        queryWrapper.last(SqlConstant.SQL_LIMIT_1);
        return this.getOne(queryWrapper);
    }
}
