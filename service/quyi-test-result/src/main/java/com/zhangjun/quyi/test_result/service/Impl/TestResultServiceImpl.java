package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.resultVo.DataTree;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.test_result.api.TestConfigApi;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.dto.TestResultDto;
import com.zhangjun.quyi.test_result.entity.vo.TestResultQueryVo;
import com.zhangjun.quyi.test_result.mapper.TestResultServiceMapper;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import com.zhangjun.quyi.test_result.service.TestResultService;
import com.zhangjun.quyi.utils.FileUtil;
import com.zhangjun.quyi.utils.JsonUtil;
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
            testResultInfoService.save(item);
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
        one.setRun_success_rate( Double.valueOf(String.format("%.2f",successCount / Double.valueOf(one.getRun_num()))));
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
        }
        else testresult.setRun_error_num(1);
        this.save(testresult);
        TestResult one = this.getOne(queryWrapper);
        List<TestResultInfo> testResultInfoList = testResultDto.getTestResultInfoList();
        testResultInfoList.stream().forEach(item->{
            item.setResult_id(one.getResult_id());
            item.setPlatform_id(configId);
            testResultInfoService.save(item);
        });
        return true;
    }


}
