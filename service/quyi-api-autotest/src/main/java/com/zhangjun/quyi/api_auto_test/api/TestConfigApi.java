package com.zhangjun.quyi.api_auto_test.api;

import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * test-config服务接口
 */
@FeignClient(value = "test-config")
public interface TestConfigApi {

    /**
     * 按照指定id查询配置
     * @param configId
     * @return
     */
    @GetMapping("/api/test_config/selectConfig/{configId}")
    public ResultModel selectConfigById(@PathVariable(required=true) String configId);


    /**
     * 获取配置文件
     * @return
     */
    @GetMapping("/api/test_config/selectConfigPath")
    @ApiOperation(value = "获取全局配置文件路径")
    public ResultModel getConfigPath();

}
