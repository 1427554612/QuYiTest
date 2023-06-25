package com.zhangjun.quyi.test_result.api;

import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "test-config")
@Component
public interface TestConfigApi {

    /**
     * 获取配置文件
     * @return
     */
    @GetMapping("/api/test_config/selectConfigPath")
    @ApiOperation(value = "获取全局配置文件路径")
    public ResultModel getConfigPath();

    /**
     * 根据id查询配置
     * @param configId
     * @return
     */
    @GetMapping("/api/test_config/selectConfig/{configId}")
    @ApiOperation(value = "通过id查询配置")
    public ResultModel selectConfigById(@PathVariable(name = "configId") String configId);

}
