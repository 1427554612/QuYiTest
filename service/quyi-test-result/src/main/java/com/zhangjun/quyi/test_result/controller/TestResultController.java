package com.zhangjun.quyi.test_result.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.dto.TestResultDto;
import com.zhangjun.quyi.test_result.entity.vo.TestResultQueryVo;
import com.zhangjun.quyi.test_result.service.TestResultService;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Api(description = "结果管理")
@RequestMapping("/api/test_result")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 组合查询带分页查询结果
     * @return
     * @throws Exception
     */
    @PostMapping("/findResult/{current}/{size}")
    @ApiOperation("组合查询带分页查询结果")
    public ResultModel findResult(@ApiParam(name = "current",value = "当前页")
                                      @PathVariable int current,
                                  @ApiParam(name = "size",value = "数量")
                                  @PathVariable int size,
                                  @RequestBody TestResultQueryVo testResultQueryVo) throws Exception {
        Page<TestResult> resultPage = testResultService.findResult(current,size,testResultQueryVo);
        List<TestResultDto> testResultDtos = new ArrayList<>();
        resultPage.getRecords().stream().forEach(testResult -> {
            TestResultDto testResultDto = new TestResultDto();
            BeanUtils.copyProperties(testResult,testResultDto);
            testResultDtos.add(testResultDto);
        });
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,testResultDtos).data(HttpConstant.RESPONSE_STR_TOTAL,resultPage.getTotal());
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
    @ApiOperation("更新结果记录")
    public ResultModel updateResult(@ApiParam(name = "configId",value = "配置id")@PathVariable String configId,
                                    @ApiParam(name = "testResultDto",value = "测试结构dto")
                                    @RequestBody TestResultDto testResultDto) throws Exception {
        redisTemplate.delete("test-result::log");
        return testResultService.updateResult(testResultDto,configId) == true ? ResultModel.ok(): ResultModel.error();
    }

    /**
     * 添加结果
     * @return
     */
    @PostMapping("/saveResult/{configId}")
    @ApiOperation("添加结果")
    public ResultModel saveResult(@ApiParam(name = "configId",value = "配置id")@PathVariable String configId,
                                  @ApiParam(name = "testResultDto",value = "测试结构dto")
                                  @RequestBody TestResultDto testResultDto) throws Exception {
        redisTemplate.delete("test-result::log");
        return testResultService.saveResult(configId,testResultDto) == true ? ResultModel.ok(): ResultModel.error();
    }

}
