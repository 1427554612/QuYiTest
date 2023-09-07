package com.zhangjun.quyi.test_result.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.vo.TestResultQueryVo;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import com.zhangjun.quyi.test_result.service.TestResultService;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Api(description = "结果管理")
@RequestMapping("/api/test_result")
@Validated
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private TestResultInfoService testResultInfoService;


    /**
     * 组合查询带分页查询结果
     * @return
     * @throws Exception
     */
    @PostMapping("/findResult/{current}/{size}")
    @ApiOperation("组合查询带分页查询结果")
    public ResultModel findResult(@ApiParam(name = "current",value = "当前页")
                                      @PathVariable Integer current,
                                  @ApiParam(name = "size",value = "数量")
                                  @PathVariable Integer size,
                                  @RequestBody TestResultQueryVo testResultQueryVo){
        Page<TestResult> resultPage = testResultService.findResult(current,size,testResultQueryVo);
        List<TestResult> testResultDtos = new ArrayList<>();
        resultPage.getRecords().stream().forEach(testResult -> {
            TestResult testResultDto = new TestResult();
            BeanUtils.copyProperties(testResult,testResultDto);
            testResultDtos.add(testResultDto);
        });
        return ResultModel.ok()
                .data(HttpConstant.RESPONSE_STR_LIST,testResultDtos)
                .data(HttpConstant.RESPONSE_STR_TOTAL,resultPage.getTotal());
    }


    /**
     * 查询所有结果
     * @return
     */
    @GetMapping("/findResult")
    @ApiOperation("查询所有结果")
    public ResultModel findResult(){
        List<TestResult> list = testResultService.findResult();
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,list);
    }

    /**
     * 查询该id的数据
     * @return
     */
    @GetMapping("/findById/{resultId}")
    @ApiOperation("查询该id的数据")
    public ResultModel findById(@ApiParam(name = "resultId",value = "结果id")@PathVariable String resultId){
        TestResult testResult = testResultService.findById(resultId);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,testResult);
    }


        /**
         * 查询日志文件树
         * @return
         * @throws Exception
         */
    @GetMapping("/findLogTree")
    @ApiOperation("查询日志文件树")
    public ResultModel findLogTree() throws Exception {
        return ResultModel.ok()
                .data(HttpConstant.RESPONSE_STR_LIST,testResultService.findLogTree());
    }

    /**
     * 查询日志文件树
     * @return
     * @throws Exception
     */
    @PostMapping("/findLog")
    @ApiOperation("查询指定日志")
    @Cacheable(value = {"test-result"},keyGenerator = "redisKeyGenerator")
    public ResultModel findLog(@RequestBody Map<String,Object> queryMap) throws Exception {
        return ResultModel.ok()
                .data(HttpConstant.RESPONSE_STR_DATA,testResultService.findLog(queryMap));
    }


    /**
     * 清空运行日志
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteLog")
    @ApiOperation("清空运行日志")
    public ResultModel deleteLog() throws Exception {
        return ResultModel.ok();
    }


    /***********************  remote api ******************************/

    @GetMapping("/findResultByCaseName/{caseName}")
    @ApiOperation(value = "根据用例名称查询结果")
    public ResultModel findResultByCaseName(@ApiParam(name = "caseName",value = "用例名称")
                                                @PathVariable String caseName){
        QueryWrapper<TestResult> testResultQueryWrapper = new QueryWrapper<>();
        testResultQueryWrapper.eq("case_name",caseName);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,testResultService.getOne(testResultQueryWrapper));
    }

    /**
     * 修改结果
     * @return
     */
    @PutMapping("/updateResult")
    @Caching(evict = {@CacheEvict(value = "test-result",key = "'log'"),
            @CacheEvict(value = "test-charts",key = "'successRate'"),
            @CacheEvict(value = "test-charts",key = "'platformSuccessAndErrorNum'"),
            @CacheEvict(value = "test-charts",key = "'currentSuccessAndErrorNum'")})
    @ApiOperation("更新结果记录")
    public ResultModel updateResult(@ApiParam(name = "testResult",value = "结果对象")
                                    @RequestBody Map<String,Object> testResultMaps) throws Exception {
        String s = JsonUtil.objectMapper.writeValueAsString(testResultMaps);
        String testResult = JsonUtil.objectMapper.readTree(s).get("testResult").toString();
        String testResultInfo = JsonUtil.objectMapper.readTree(s).get("testResultInfo").toString();;
        return testResultService.updateResult(JsonUtil.objectMapper.readValue(testResult,TestResult.class)
                ,JsonUtil.objectMapper.readValue(testResultInfo,TestResultInfo.class)) == true ? ResultModel.ok(): ResultModel.error();
    }

    /**
     * 添加结果
     * @return
     */
    @PostMapping("/saveResult/{configId}")
    @CacheEvict(value = "test-result",key = "'log'")
    @ApiOperation("添加结果")
    public ResultModel saveResult(@ApiParam(name = "configId",value = "配置id")@PathVariable String configId,
                                  @ApiParam(name = "testResult",value = "测试结果对象")
                                  @RequestBody TestResult testResult) throws Exception {
        return testResultService.saveResult(configId,testResult) == true ? ResultModel.ok(): ResultModel.error();
    }

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
     * 清空所有运行结果数据
     * @return
     */
    @DeleteMapping("/deleteAllResult")
    @Caching(evict = {
            @CacheEvict(value = "test-charts",key = "'successRate'"),
            @CacheEvict(value = "test-charts",key = "'platformSuccessAndErrorNum'"),
            @CacheEvict(value = "test-charts",key = "'currentSuccessAndErrorNum'"),
            @CacheEvict(value = "test-result",key = "'log'")
    })
    @ApiOperation("清空所有运行结果数据")
    public ResultModel deleteAllResult(){
        testResultService.deleteAllResult();
        return ResultModel.ok();
    }
}
