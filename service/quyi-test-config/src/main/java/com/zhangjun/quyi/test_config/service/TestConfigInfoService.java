package com.zhangjun.quyi.test_config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.quyi.test_config.entity.TestConfigInfo;

public interface TestConfigInfoService extends IService<TestConfigInfo> {

    /**
     * 保存配置详情信息
     * @param testConfigInfo
     * @return
     */
    TestConfigInfo saveTestConfigInfo(TestConfigInfo testConfigInfo);


    /**
     * 查询最新一条配置详情
     * @return
     */
    TestConfigInfo findLastTestConfigInfo();
}
