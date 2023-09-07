package com.zhangjun.quyi.test_result.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import com.zhangjun.quyi.test_result.service.TestResultTempInfoService;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "结果详情管理")
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

    /**
     * 通过resultId查询所有执行详情记录
     * @return
     */
    @GetMapping("/getAll/{resultId}/{current}/{size}")
    @ApiOperation("通过resultId查询所有执行详情记录")
    public ResultModel findAllInfoByResultId(@PathVariable(name = "resultId") String resultId,
                                             @PathVariable(name = "current") Integer current,
                                             @PathVariable(name = "size") Integer size){
        IPage<TestResultInfo> pages = testResultInfoService.findAllInfoByResultId(resultId, current, size);
        return ResultModel.ok().data("total",pages.getTotal()).data(HttpConstant.RESPONSE_STR_LIST,pages.getRecords());
    }


    /**
     * 查询这个结果下的所有结果详情记录
     * @return
     * @throws Exception
     */
    @GetMapping ("/findList/{resultId}/{sort}")
    @ApiOperation("查询这个结果下的所有结果详情记录")
    public ResultModel findResultInfoList(@ApiParam(name = "resultId",value = "结果id")
                                          @PathVariable String resultId,
                                          @ApiParam(name = "sort",value = "排序方式")
                                          @Range(max = 2, min = 1,message="排序方式只能为1/2") @PathVariable Integer sort
    ){
        List<TestResultInfo> testResultInfoList = testResultInfoService.findResultInfoList(resultId,sort);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,testResultInfoList);
    }


}
