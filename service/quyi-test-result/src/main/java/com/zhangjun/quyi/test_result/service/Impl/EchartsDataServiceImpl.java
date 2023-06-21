package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.mapper.TestResultServiceMapper;
import com.zhangjun.quyi.test_result.service.EchartsDataService;
import com.zhangjun.quyi.utils.JsonUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EchartsDataServiceImpl  extends ServiceImpl<TestResultServiceMapper, TestResult> implements EchartsDataService  {

    private static Logger logger = LoggerFactory.getLogger(EchartsDataServiceImpl.class);


    /**
     * 以名称分组统计用例执行成功率
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public Map<String, Object> getCaseSuccessRate() throws JsonProcessingException {
        QueryWrapper<TestResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("case_name","run_success_rate");
        queryWrapper.groupBy("case_name");
        List<TestResult> testResultList = this.list(queryWrapper);
        List<Map<String,Object>> series= new ArrayList<>();
        List<String> legends = new ArrayList<>();
        testResultList.stream().forEach(testResult -> {
            Map<String,Object> keyValueMap = new HashMap<>();
            keyValueMap.put("value",testResult.getRun_success_rate());
            keyValueMap.put("name",testResult.getCase_name());
            series.add(keyValueMap);
            legends.add(testResult.getCase_name());
        });
        logger.info("查询testResultList = "+ JsonUtil.objectMapper.writeValueAsString(testResultList));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("legends",legends);
        resultMap.put("series",series);
        return resultMap;
    }
}
