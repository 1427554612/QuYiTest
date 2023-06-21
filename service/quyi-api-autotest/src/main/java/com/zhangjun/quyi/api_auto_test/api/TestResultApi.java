package com.zhangjun.quyi.api_auto_test.api;

import com.zhangjun.quyi.api_auto_test.entity.dto.TestResultDto;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * test-result服务接口
 */
@Component
@FeignClient(value = "test-result")
public interface TestResultApi {

    /**
     * 更具用例名称查询结果
     * @param caseName
     * @return
     */
    @GetMapping("/api/test_result/findResultByCaseName/{caseName}")
    public ResultModel findResultByCaseName(@PathVariable String caseName);

    /**
     * 修改结果
     * @return
     */
    @PutMapping("/api/test_result/updateResult")
    public ResultModel updateResult(@RequestBody TestResultDto testResultDto);


    /**
     * 添加结果
     * @return
     */
    @PostMapping("/api/test_result/saveResult")
    public ResultModel saveResult(@RequestBody TestResultDto testResultDto);

}
