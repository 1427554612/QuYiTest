package com.zhangjun.quyi.test_task.api;

import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Component
@FeignClient("api-auto-test")
public interface ApiAutoTestApi {


    /**
     * 获取所有测试用例
     * @return
     */
    @GetMapping("/api/api-autotest/selectAllCase")
    public ResultModel selectAllCase();

    /**
     * 批量执行用例
     * @param caseList
     * @return
     */
    @PostMapping("/api/api-autotest/runCase/{configId}")
    @ApiOperation(value = "批量执行测试用例")
    public ResultModel runCase(@RequestBody ArrayList<String> caseList,
                               @PathVariable(value = "configId") String configId);

}
