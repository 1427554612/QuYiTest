package com.zhangjun.quyi.test_result.controller;

import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import com.zhangjun.quyi.test_result.service.TestResultTempInfoService;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "结果管理")
@RequestMapping("/api/test_result/info")
@Validated
public class TestResultInfoController {

    @Autowired
    private TestResultInfoService testResultInfoService;

    @Autowired
    private TestResultTempInfoService testResultTempInfoService;

    /**
     * 添加结果详情
     * @return
     */
    @PostMapping("/saveResultInfo")
    @CacheEvict(value = "test-result",key = "'log'")
    @ApiOperation("添加结果详情")
    public ResultModel saveResultInfo(@ApiParam(name = "testResultInfo",value = "结果详情id")
                                  @RequestBody TestResultInfo testResultInfo) throws Exception {
        TestResultInfo resultInfo = testResultInfoService.saveResultInfo(testResultInfo);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,resultInfo);
    }

    /**
     * 清空临时结果数据
     * @return
     */
    @DeleteMapping("/clear")
    @CacheEvict(value = "test-result",key = "'log'")
    @ApiOperation("清空临时结果数据")
    public ResultModel clearResultTemp(){
        return testResultTempInfoService.remove(null) ? ResultModel.ok() : ResultModel.error();
    }


}
