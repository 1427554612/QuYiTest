package com.zhangjun.quyi.test_result.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.dto.TestResultDto;
import com.zhangjun.quyi.test_result.entity.vo.TestResultQueryVo;
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
        List<TestResultDto> testResultDtos = new ArrayList<>();
        resultPage.getRecords().stream().forEach(testResult -> {
            TestResultDto testResultDto = new TestResultDto();
            BeanUtils.copyProperties(testResult,testResultDto);
            testResultDtos.add(testResultDto);
        });
        return ResultModel.ok()
                .data(HttpConstant.RESPONSE_STR_LIST,testResultDtos)
                .data(HttpConstant.RESPONSE_STR_TOTAL,resultPage.getTotal());
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @GetMapping ("/findResultInfoList/{resultId}/{sort}")
    @ApiOperation("结果id查询出所有结果详情")
    public ResultModel findResultInfoList(@ApiParam(name = "resultId",value = "结果id")
                                  @PathVariable String resultId,
                                          @ApiParam(name = "sort",value = "排序方式")
                                          @Range(max = 2, min = 1,message="排序方式只能为1/2") @PathVariable Integer sort
                                  ){
        List<TestResultInfo> testResultInfoList = testResultService.findResultInfoList(resultId,sort);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,testResultInfoList);
    }

    /**
     * 查询所有结果
     * @return
     */
    @GetMapping("/findResult")
    @ApiOperation("查询所有结果")
    @Cacheable(value = "test-result",key = "'log'",cacheManager = "cacheManager1Minute")
    public ResultModel findResult(){
        List<TestResultDto> list = testResultService.findResult();
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,list);
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
    @PutMapping("/updateResult/{configId}")
    @Caching(evict = {@CacheEvict(value = "test-result",key = "'log'"),
            @CacheEvict(value = "test-charts",key = "'successRate'"),
            @CacheEvict(value = "test-charts",key = "'platformSuccessAndErrorNum'"),
            @CacheEvict(value = "test-charts",key = "'currentSuccessAndErrorNum'")})
    @ApiOperation("更新结果记录")
    public ResultModel updateResult(@ApiParam(name = "configId",value = "配置id")@PathVariable String configId,
                                    @ApiParam(name = "testResultDto",value = "测试结构dto")
                                    @RequestBody TestResultDto testResultDto) throws Exception {
        return testResultService.updateResult(testResultDto,configId) == true ? ResultModel.ok(): ResultModel.error();
    }

    /**
     * 添加结果
     * @return
     */
    @PostMapping("/saveResult/{configId}")
    @CacheEvict(value = "test-result",key = "'log'")
    @ApiOperation("添加结果")
    public ResultModel saveResult(@ApiParam(name = "configId",value = "配置id")@PathVariable String configId,
                                  @ApiParam(name = "testResultDto",value = "测试结构dto")
                                  @RequestBody TestResultDto testResultDto) throws Exception {
        return testResultService.saveResult(configId,testResultDto) == true ? ResultModel.ok(): ResultModel.error();
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
