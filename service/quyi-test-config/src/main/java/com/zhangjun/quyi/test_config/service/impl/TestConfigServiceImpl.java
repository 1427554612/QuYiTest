package com.zhangjun.quyi.test_config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.test_config.entity.TestConfig;
import com.zhangjun.quyi.test_config.entity.vo.TestConfigQueryVo;
import com.zhangjun.quyi.test_config.mapper.TestConfigMapper;
import com.zhangjun.quyi.test_config.service.TestConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.quyi.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-05-04
 */
@Service
public class TestConfigServiceImpl extends ServiceImpl<TestConfigMapper, TestConfig> implements TestConfigService {

    private Logger logger  = LoggerFactory.getLogger(TestConfigServiceImpl.class);


    /**
     * 添加配置信息
     * @param testConfig
     * @return
     */
    public TestConfig saveConfig(TestConfig testConfig){
        if(this.save(testConfig)==false) throw new ExceptionEntity(20001,"添加配置失败....");
        QueryWrapper<TestConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(HttpConstant.API_STR_CONFIG_NAME,testConfig.getConfigName());
        return this.getOne(queryWrapper);
    }

    /**
     * 组合查询带分页查询配置
     * @param current
     * @param size
     * @param testConfigQueryVo
     * @return
     */
    @Override
    public IPage selectConfig(Integer current, Integer size, TestConfigQueryVo testConfigQueryVo) {
        Page<TestConfig> page = new Page<>(current,size);
        String configId = testConfigQueryVo.getConfigId();
        String configName = testConfigQueryVo.getConfigName();
        String configType = testConfigQueryVo.getConfigType();
        Date beginTime = testConfigQueryVo.getBeginTime();
        Date endTime = testConfigQueryVo.getEndTime();
        String updateUp = testConfigQueryVo.getUpdateUp();
        QueryWrapper wrapper = new QueryWrapper<TestConfig>();
        if (!StringUtils.isEmpty(configId)) wrapper.eq(HttpConstant.API_STR_CONFIG_ID,configId);
        if (!StringUtils.isEmpty(configName)) wrapper.eq(HttpConstant.API_STR_CONFIG_NAME,configName);
        if (!StringUtils.isEmpty(configType)) wrapper.eq(HttpConstant.API_STR_CONFIG_TYPE,configType);
        if (null!=beginTime) wrapper.le(HttpConstant.API_STR_CREATE_TIME,beginTime);
        if (null!=endTime) wrapper.ge(HttpConstant.API_STR_CREATE_TIME,endTime);
        if (!StringUtils.isEmpty(updateUp)) wrapper.eq("update_up",updateUp);
        wrapper.orderByDesc(HttpConstant.API_STR_CREATE_TIME);
        IPage resultPage = this.page(page, wrapper);
        return resultPage;
    }
}
