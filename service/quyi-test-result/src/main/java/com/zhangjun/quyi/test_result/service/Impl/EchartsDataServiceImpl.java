package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.test_result.api.TestConfigApi;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.mapper.TestResultInfoServiceMapper;
import com.zhangjun.quyi.test_result.mapper.TestResultServiceMapper;
import com.zhangjun.quyi.test_result.service.EchartsDataService;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.ResultModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EchartsDataServiceImpl  extends ServiceImpl<TestResultServiceMapper, TestResult> implements EchartsDataService  {

    private static Logger logger = LoggerFactory.getLogger(EchartsDataServiceImpl.class);

    @Autowired
    private TestResultInfoServiceMapper testResultInfoServiceMapper;

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
        wrapper.groupBy("platform_id");
        wrapper.select("platform_Id,sum(case when run_result=1 then 1 else 0 end) as 'successNum',\n" +
                "sum(case when run_result=0 then 1 else 0 end) as 'errorNum'");

        List<Map<String, Object>> maps = testResultInfoServiceMapper.selectMaps(wrapper);
        String configName = "";
        for (int i = 0; i < maps.size(); i++) {
            ResultModel resultModel = testConfigApi.selectConfigById((String) maps.get(i).get("platform_Id"));
            Object data = resultModel.getData().get("data");
            configName = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(data)).get("configName").asText();
            keys.add(configName);
            System.out.println("map返回的数据：" + JsonUtil.objectMapper.writeValueAsString(maps.get(i)));
            Object successNum = maps.get(i).get("successNum");
            Object errorNum = maps.get(i).get("errorNum");
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
     * 获取当前成功和失败的用例
     * @return
     */
    @Override
    public Map<String, Object> getCurrentSuccessAndErrorNum() {
        return null;
    }
}
