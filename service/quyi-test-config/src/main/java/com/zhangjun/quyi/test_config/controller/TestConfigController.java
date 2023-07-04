package com.zhangjun.quyi.test_config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.test_config.entity.TestConfig;
import com.zhangjun.quyi.test_config.entity.vo.TestConfigQueryVo;
import com.zhangjun.quyi.test_config.mapper.TestConfigMapper;
import com.zhangjun.quyi.test_config.service.TestConfigService;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-05-04
 */
@RestController
@RequestMapping("/api/test_config")
@Api(description = "测试配置管理")
@RefreshScope
public class TestConfigController {

    @Autowired
    private TestConfigService testConfigService;

    @Value("${python.config.path}")
    private String configPath;

    @Value("${python.log.path}")
    private String logPath;

    @Value("${python.log.reportPath}")
    private String reportPath;

    @Value("${python.project.path}")
    private String pythonProjectPath;

    @Value("${python.api.runtime.path}")
    private String pythonApiRuntimePath;

    @Value("${proxy.config.path}")
    private String proxyConfigPath;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 添加测试配置
     * @param testConfig
     * @return
     */
    @PostMapping("/saveTestConfig")
    @CachePut(value = "TestConfig",key="#result.data.get('testConfig').configId",cacheManager = "cacheManager1Hour")
    @Caching(evict ={@CacheEvict(value = "TestConfig",key = "'list'"),
            @CacheEvict(value = "TestConfig",key = "'page_1_10'"),
            @CacheEvict(value = "TestConfig",key = "'page_2_10'"),
            @CacheEvict(value = "TestConfig",key = "'page_3_10'")})
    @ApiOperation(value = "添加测试配置")
    public ResultModel saveTestConfig(@ApiParam(name = "testConfig",value = "配置对象")
                                          @Validated @RequestBody TestConfig testConfig){
        return ResultModel.ok().data("testConfig",testConfigService.saveConfig(testConfig));
    }

    /**
     * 删除测试配置
     * @param configId
     * @return
     */
    @DeleteMapping("/deleteTestConfig/{configId}")
    @CacheEvict(value="TestConfig",key = "#configId")
    @ApiOperation(value = "删除测试配置")
    public ResultModel deleteTestConfig(@ApiParam(name = "configId",value = "配置id")
                                      @PathVariable String configId){
        redisTemplate.delete("TestConfig::page_1_10");
        return testConfigService.removeById(configId) ? ResultModel.ok() : ResultModel.error();
    }


    /**
     * 修改测试配置
     * @param configId
     * @param testConfig
     * @return
     */
    @PutMapping("/updateTestConfig/{configId}")
    @CachePut(value = "TestConfig",key="#configId",cacheManager = "cacheManager1Hour")
    @ApiOperation(value = "修改测试配置")
    public ResultModel updateTestConfig(@ApiParam(name = "configId",value = "用例id")
                                      @PathVariable String configId,
                                      @ApiParam(name = "testConfig",value = "配置对象")
                                      @Validated @RequestBody TestConfig testConfig){
        testConfig.setConfigId(configId);
        return testConfigService.updateById(testConfig) ? ResultModel.ok().data("data",testConfig) : ResultModel.error();
    }

    /**
     * 查询所有配置
     * @return
     */
    @GetMapping("/selectConfig")
    @Cacheable(value = "TestConfig",key = "'list'",cacheManager = "cacheManager1Hour")
    @ApiOperation(value = "查询所有配置")
    public ResultModel selectConfig(){
        QueryWrapper<TestConfig> testConfigQueryWrapper = new QueryWrapper<>();
        testConfigQueryWrapper.orderByDesc("update_time");
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,testConfigService.list(testConfigQueryWrapper));
    }

    /**
     * 组合查询带分页查询配置
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/selectConfig/{current}/{size}")
    @Cacheable(value = "TestConfig",key = "'page_' +#current+'_'+#size",cacheManager = "cacheManager1Hour")
    @ApiOperation(value = "分页组合条件查询")
    public ResultModel selectConfig(@ApiParam(name = "current",value = "当前页")
                                        @PathVariable Integer current,
                                        @ApiParam(name = "size",value = "查询数量")
                                        @PathVariable Integer size,
                                        @ApiParam(name = "testConfigController",value = "查询vo对象")
                                        @Validated @RequestBody TestConfigQueryVo testConfigQueryVo){
        List<TestConfig> testConfigs = testConfigService.selectConfig(current,size,testConfigQueryVo);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,testConfigs).data(HttpConstant.RESPONSE_STR_TOTAL,testConfigs.size());
    }

    /**
     * 获取全局Config配置文件路径
     * @return
     */
    @GetMapping("/selectConfigPath")
    @Cacheable(value = "ConfigPath",key = "'configPath'",cacheManager = "cacheManager1Day")
    @ApiOperation(value = "获取全局配置文件路径")
    public ResultModel getConfigPath(){
        return ResultModel.ok()
                .data("configPath", configPath)
                .data("logPath", logPath)
                .data("reportPath", reportPath)
                .data("pythonProjectPath", pythonProjectPath)
                .data("apiRunTimePath", pythonApiRuntimePath)
                .data("proxyConfigPath",proxyConfigPath);
    }


    /* ------------------------------remote api-------------------------------------- */

    /**
     * 通过id查询配置
     * @param configId
     * @return
     */
    @GetMapping("/selectConfig/{configId}")
    @Cacheable(value = "TestConfig",key = "#configId",cacheManager = "cacheManager1Minute")
    @ApiOperation(value = "通过id查询配置")
    public ResultModel selectConfigById(@ApiParam(name = "configId",value = "用例id")
                                        @PathVariable String configId){
        return ResultModel.ok().data("testConfig",testConfigService.getById(configId));
    }


}

