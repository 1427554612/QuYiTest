package com.zhangjun.quyi.test_result.api;

import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

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
}
