package com.zhangjun.quyi.test_config.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhangjun.quyi.test_config.entity.TestConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.quyi.test_config.entity.vo.TestConfigQueryVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2023-05-04
 */
public interface TestConfigService extends IService<TestConfig> {

    /**
     * 组合查询带分页查询配置
     * @param current
     * @param size
     * @param testConfigQueryVo
     * @return
     */
    IPage selectConfig(Integer current, Integer size, TestConfigQueryVo testConfigQueryVo);

    /**
     * 添加配置
     * @param testConfig
     * @return
     */
    TestConfig saveConfig(TestConfig testConfig);
}
