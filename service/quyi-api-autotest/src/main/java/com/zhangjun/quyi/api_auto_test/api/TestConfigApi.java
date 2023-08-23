package com.zhangjun.quyi.api_auto_test.api;

import com.zhangjun.quyi.api_auto_test.entity.remoteEntity.TestConfigInfo;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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


    /**
     * 保存配置详情
     * @return
     */
    @PostMapping("/api/test_config/saveTestConfigInfo")
    @ApiOperation(value = "保存配置详情")
    public ResultModel saveTestConfigInfo(@RequestBody TestConfigInfo testConfigInfo);


}
