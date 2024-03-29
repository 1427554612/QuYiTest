package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.test_result.api.TestConfigApi;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.TestResultTempInfo;
import com.zhangjun.quyi.test_result.mapper.TestResultInfoServiceMapper;
import com.zhangjun.quyi.test_result.mapper.TestResultServiceMapper;
import com.zhangjun.quyi.test_result.mapper.TestResultTempInfoMapper;
import com.zhangjun.quyi.test_result.service.EchartsDataService;
import com.zhangjun.quyi.test_result.service.TestResultTempInfoService;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.ResultModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.*;

@Service
public class EchartsDataServiceImpl  extends ServiceImpl<TestResultServiceMapper, TestResult> implements EchartsDataService  {

    private static Logger logger = LoggerFactory.getLogger(EchartsDataServiceImpl.class);

    @Autowired
    private TestResultInfoServiceMapper testResultInfoServiceMapper;

    @Autowired
    private TestResultTempInfoService testResultTempInfoService;

    @Autowired
    private TestConfigApi testConfigApi;


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
            keyValueMap.put("value",testResult.getRunSuccessRate());
            keyValueMap.put("name",testResult.getCaseName());
            series.add(keyValueMap);
            legends.add(testResult.getCaseName());
        });
        logger.info("查询testResultList = "+ JsonUtil.objectMapper.writeValueAsString(testResultList));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("legends",legends);
        resultMap.put("series",series);
        return resultMap;
    }

    /**
     * 以平台区分成功总数和失败总数
     * @return
     */
    @Override
    public Map<String, Object> getPlatformSuccessAndErrorNum() throws JsonProcessingException {
        List<String> keys  = new ArrayList<>();
        List<Integer> successPlatform = new ArrayList<>();
        List<Integer> errorPlatform = new ArrayList<>();
        QueryWrapper<TestResultInfo> wrapper = new QueryWrapper<>();
        wrapper.groupBy("config_id");
        wrapper.select("config_id,sum(case when run_result=1 then 1 else 0 end) as 'successNum',\n" +
                "sum(case when run_result=0 then 1 else 0 end) as 'errorNum'");

        List<Map<String, Object>> maps = testResultInfoServiceMapper.selectMaps(wrapper);
        String configName = "";
        for (int i = 0; i < maps.size(); i++) {
            ResultModel resultModel = testConfigApi.selectConfigById((String) maps.get(i).get("config_id"));
            Object data = resultModel.getData().get("testConfig");
            configName = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(data)).get("configName").asText();
            keys.add(configName);
            successPlatform.add(Integer.parseInt(String.valueOf(maps.get(i).get("successNum"))));
            errorPlatform.add(Integer.parseInt(String.valueOf(maps.get(i).get("errorNum"))));
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("keys",keys);
        resultMap.put("successNums",successPlatform);
        resultMap.put("errorNums",errorPlatform);
        return resultMap;
    }

    /**
     * selectMaps == [{"successNum":14,"errorNum":1}]
     * 获取当前成功和失败的用例
     * select sum(case when run_result = 1 then 1 else 0 end) as 'successNum',sum(case when run_result = 0 then 1 else 0 end) as 'errorNum' from test_result_temp_info
     * @return
     */
    @Override
    public Map<String, Object> getCurrentSuccessAndErrorNum() throws JsonProcessingException {
        List<String> keyList = new ArrayList<>();
        List<Map<String,Object>> valueList = new ArrayList<>();
        QueryWrapper<TestResultTempInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(case when run_result = 1 then 1 else 0 end) as 'successNum',sum(case when run_result = 0 then 1 else 0 end) as 'errorNum'");
        Map<String, Object> map = testResultTempInfoService.getMap(queryWrapper);
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            keyList.add(key);
            Map<String,Object> valueMap = new HashMap<>();
            valueMap.put("name",key);
            valueMap.put("value",next.getValue());
            valueList.add(valueMap);
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("legends",keyList);
        resultMap.put("series",valueList);
        return resultMap;
    }
}
