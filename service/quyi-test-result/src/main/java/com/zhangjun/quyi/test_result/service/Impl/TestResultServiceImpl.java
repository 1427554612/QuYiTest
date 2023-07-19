package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.resultVo.DataTree;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.test_result.api.TestConfigApi;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.TestResultTempInfo;
import com.zhangjun.quyi.test_result.entity.dto.TestResultDto;
import com.zhangjun.quyi.test_result.entity.vo.TestResultQueryVo;
import com.zhangjun.quyi.test_result.mapper.TestResultServiceMapper;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import com.zhangjun.quyi.test_result.service.TestResultService;
import com.zhangjun.quyi.test_result.service.TestResultTempInfoService;
import com.zhangjun.quyi.utils.FileUtil;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.ResultModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TestResultServiceImpl extends ServiceImpl<TestResultServiceMapper, TestResult> implements TestResultService {

    @Autowired
    private TestConfigApi testConfigApi;

    @Autowired
    private TestResultInfoService testResultInfoService;

    @Autowired
    private TestResultTempInfoService testResultTempInfoService;

    /**
     * 查询日志文件树
     * @return
     */
    @Override
    public List<DataTree> findLogTree() throws Exception {
        String logPath = (String) testConfigApi.getConfigPath().getData().get("logPath");
        ArrayList<DataTree> dataTrees = new ArrayList<>();
        dataTrees.add(FileUtil.initFileTree(logPath));
        return dataTrees;
    }

    /**
     * 查询指定日志
     * @param queryMap
     */
    @Override
    public String findLog(Map<String, Object> queryMap) throws Exception {
        return FileUtil.readLogByQueryMap(queryMap);
    }


    /**
     * 查询所有结果和详情
     * @return
     */
    @Override
    public List<TestResultDto> findResult() {
        List<TestResult> TestResults = this.list();
        List<TestResultDto> testResultDtos = new ArrayList<>();
        TestResults.stream().forEach( item ->{
            TestResultDto testResultDto = new TestResultDto();
            BeanUtils.copyProperties(item,testResultDto);
            String result_id = testResultDto.getResult_id();
            QueryWrapper<TestResultInfo> qw = new QueryWrapper<>();
            qw.eq("result_id",result_id);
            List<TestResultInfo> list = testResultInfoService.list(qw);
            testResultDto.setTestResultInfoList(list);
            testResultDtos.add(testResultDto);
        });
        return testResultDtos;
    }

    /**
     * 条件查询带分页
     * @param current
     * @param size
     * @param testResultQueryVo
     * @return
     */
    @Override
    public Page<TestResult> findResult(int current, int size, TestResultQueryVo testResultQueryVo) {
        Page<TestResult> page = new Page<>(current,size);
        QueryWrapper<TestResult> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(testResultQueryVo.getResult_id()))queryWrapper.eq("result_id",testResultQueryVo.getResult_id());
        if (!StringUtils.isEmpty(testResultQueryVo.getCase_name()))queryWrapper.eq("case_name",testResultQueryVo.getCase_name());
        if (!StringUtils.isEmpty(testResultQueryVo.getCase_type()))queryWrapper.eq("case_type",testResultQueryVo.getCase_type());
        Page<TestResult> resultPage = this.page(page,queryWrapper);
        return resultPage;
    }

    /**
     * 修改测试结果
     * @param testResultDto
     * @return
     */
    @Override
    public boolean updateResult(TestResultDto testResultDto,String configId) throws Exception {
        String case_name = testResultDto.getCase_name();
        QueryWrapper<TestResult> wrapper = new QueryWrapper<>();
        wrapper.eq("case_name",case_name);
        TestResult one = this.getOne(wrapper);
        if (null == one) throw new ExceptionEntity(20001,"结果不存在,无法修改......");
        // 先将新的数据插入从表
        testResultDto.getTestResultInfoList().stream().forEach(item->{
            item.setResult_id(one.getResult_id());
            item.setPlatform_id(configId);
            // 数据插入详情表
            testResultInfoService.save(item);
            // 查询添加的详情表数据
            QueryWrapper<TestResultInfo> resultInfoQueryWrapper = new QueryWrapper<>();
            resultInfoQueryWrapper.eq("result_id",item.getResult_id())
                    .eq("platform_Id",item.getPlatform_id())
                    .eq("run_begin_time",item.getRun_begin_time())
                    .eq("run_end_time",item.getRun_end_time())
                    .eq("run_time",item.getRun_time());
            TestResultInfo idTestResult = testResultInfoService.getOne(resultInfoQueryWrapper);
            // 查询临时表是否存在对应数据
            QueryWrapper<TestResultTempInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("result_id",idTestResult.getResult_id());
            TestResultTempInfo queryTempResultInfo = testResultTempInfoService.getOne(queryWrapper);
            BeanUtils.copyProperties(idTestResult,queryTempResultInfo);
            // 删除这条记录
            QueryWrapper<TestResultTempInfo> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq("result_id",idTestResult.getResult_id());
            testResultTempInfoService.remove(updateWrapper);

            // 再次插入这条记录
            testResultTempInfoService.save(queryTempResultInfo);
        });
        QueryWrapper<TestResultInfo> testResultInfoQueryWrapperSuccess = new QueryWrapper<>();
        QueryWrapper<TestResultInfo> testResultInfoQueryError = new QueryWrapper<>();

        // 统计成功总数和失败总数
        testResultInfoQueryWrapperSuccess.eq("result_id",one.getResult_id());
        testResultInfoQueryWrapperSuccess.eq("run_result",1);
        int successCount = testResultInfoService.count(testResultInfoQueryWrapperSuccess);
        testResultInfoQueryError.eq("result_id",one.getResult_id());
        testResultInfoQueryError.eq("run_result",0);
        int errorCount = testResultInfoService.count(testResultInfoQueryError);

        // 查询最近一条状态
        QueryWrapper<TestResultInfo> testResultInfoQueryWrapper1 = new QueryWrapper<>();
        testResultInfoQueryWrapper1.eq("result_id",one.getResult_id());
        testResultInfoQueryWrapper1.orderByDesc("run_begin_time");
        testResultInfoQueryWrapper1.last("limit 1");
        List<TestResultInfo> TestResultInfo = testResultInfoService.list(testResultInfoQueryWrapper1);
        com.zhangjun.quyi.test_result.entity.TestResultInfo testResultInfo = TestResultInfo.get(0);

        // 设置成功率
        one.setRun_num(successCount + errorCount);
        one.setRun_success_num(successCount);
        one.setRun_error_num(errorCount);
        one.setLast_run_date(testResultInfo.getRun_begin_time());
        one.setLast_run_result(testResultInfo.isRun_result());
        one.setLast_run_time(testResultInfo.getRun_time());

        // 设置最近执行平台
        // 设置最近一次执行的平台
        String platform_id = testResultInfo.getPlatform_id();
        ResultModel resultModel = testConfigApi.selectConfigById(platform_id);
        String configName = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(resultModel)).get("data").get("testConfig").get("configName").asText();
        one.setLast_run_platform(configName);
        Double aDouble = Double.valueOf(String.format("%.2f", successCount / Double.valueOf(one.getRun_num())));
        one.setRun_success_rate( (aDouble <= 1 ||aDouble<=1.0  ? (aDouble*100) : aDouble));
        return this.update(one,wrapper);
    }

    /**
     * 添加测试结果
     * @param testResultDto
     * @return
     */
    @Override
    public boolean saveResult(String configId,TestResultDto testResultDto) throws JsonProcessingException {
        QueryWrapper<TestResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("case_name",testResultDto.getCase_name());
        TestResult resultTestResult = this.getOne(queryWrapper);
        if (null != resultTestResult) throw new ExceptionEntity(20001,"用例已存在，无法添加结果......");
        TestResult testresult = new TestResult();
        BeanUtils.copyProperties(testResultDto,testresult);
        testresult.setRun_num(1);
        boolean run_result = testResultDto.getTestResultInfoList().get(0).isRun_result();
        if (run_result == true){
            testresult.setRun_success_num(1);
            testresult.setRun_success_rate(100.00);
            testresult.setLast_run_result(true);
        }
        else {
            testresult.setRun_error_num(1);
            testresult.setRun_success_rate(0.0);
            testresult.setLast_run_result(false);
        }
        testresult.setLast_run_time(testResultDto.getTestResultInfoList().get(0).getRun_time());
        testresult.setLast_run_date(testResultDto.getTestResultInfoList().get(0).getRun_begin_time());
        // 设置最近一次执行的平台
        ResultModel resultModel = testConfigApi.selectConfigById(configId);
        String configName = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(resultModel)).get("data").get("testConfig").get("configName").asText();
        System.out.println("最近测试平台：" + configName);
        testresult.setLast_run_platform(configName);

        this.save(testresult);
        TestResult one = this.getOne(queryWrapper);
        List<TestResultInfo> testResultInfoList = testResultDto.getTestResultInfoList();
        testResultInfoList.stream().forEach(item->{
            item.setResult_id(one.getResult_id());
            item.setPlatform_id(configId);
            // 添加到详情表中
            testResultInfoService.save(item);
            // 添加到临时表中
            QueryWrapper<TestResultInfo> resultInfoQueryWrapper = new QueryWrapper<>();
            resultInfoQueryWrapper.eq("result_id",item.getResult_id())
                    .eq("platform_Id",item.getPlatform_id())
                    .eq("run_begin_time",item.getRun_begin_time())
                    .eq("run_end_time",item.getRun_end_time())
                    .eq("run_time",item.getRun_time());
            TestResultInfo idTestResult = testResultInfoService.getOne(resultInfoQueryWrapper);
            TestResultTempInfo testResultTempInfo = new TestResultTempInfo();
            BeanUtils.copyProperties(idTestResult,testResultTempInfo);
            testResultTempInfoService.save(testResultTempInfo);
        });
        return true;
    }


    /**
     * 清空所有统计和结果数据
     */
    @Override
    public void deleteAllResult() {
        testResultInfoService.remove(null);
        testResultTempInfoService.remove(null);
        this.remove(null);
    }


}
