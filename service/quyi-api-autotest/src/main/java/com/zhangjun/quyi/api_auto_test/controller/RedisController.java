package com.zhangjun.quyi.api_auto_test.controller;

import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.service.ApiAutoTestService;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.service_base.redis.RedisKeyGenerator;
import com.zhangjun.quyi.utils.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private ApiAutoTestService apiAutoTestService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisKeyGenerator redisKeyGenerator;



    /**
     * 执行指定名称的用例,引用方式：api-autotest::allCase
     *  value:名称空间
     *  key:名称空间中的数据的名称
     * @return
     */
    @GetMapping("/addRedis")
    @Cacheable(value = "api-autotest",key = "'allCase'")
    public ResultModel list() throws Exception {
        List<ApiTestCaseEntity> caseList = apiAutoTestService.selectAllCase();
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,caseList);
    }


    @GetMapping("/addRedis/methodName")
    @Cacheable(value = "api-autotest",key = "#root.methodName")
    public ResultModel methodName() throws Exception {
        List<ApiTestCaseEntity> caseList = apiAutoTestService.selectAllCase();
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,caseList);
    }

    @GetMapping("/addRedis/uuid")
    @Cacheable(value = {"api-autotest"},keyGenerator = "redisKeyGenerator")
    public ResultModel uuid() throws Exception {
        List<ApiTestCaseEntity> caseList = apiAutoTestService.selectAllCase();
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,caseList);
    }

    @GetMapping("/getRedisValue/{key}")
    public ResultModel getRedisValue1(@PathVariable String key){
        return ResultModel.ok().data("data",redisTemplate.opsForValue().get(key));
    }
}
