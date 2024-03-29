package com.zhangjun.quyi.api_auto_test.api;

import com.zhangjun.quyi.api_auto_test.entity.remoteEntity.TestResult;
import com.zhangjun.quyi.api_auto_test.entity.remoteEntity.TestResultInfo;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResultModel findResultByCaseName(@PathVariable(name = "caseName") String caseName);

    /**
     * 修改结果
     * @return
     */
    @PutMapping("/api/test_result/updateResult")
    public ResultModel updateResult(@RequestBody Map<String,Object> testResultMaps);


    /**
     * 添加结果
     * @return
     */
    @PostMapping("/api/test_result/saveResult/{configId}")
    public ResultModel saveResult(@PathVariable(name = "configId") String configId,@RequestBody TestResult testResult);

    /**
     * 添加详情
     * @return
     */
    @PostMapping("/api/test_result/info/saveResultInfo")
    public ResultModel saveResultInfo(@RequestBody TestResultInfo testResultInfo);

    /**
     * 清空临时结果数据
     * @return
     */
    @DeleteMapping("/api/test_result/info/clear")
    public ResultModel clearResultTemp();


}
