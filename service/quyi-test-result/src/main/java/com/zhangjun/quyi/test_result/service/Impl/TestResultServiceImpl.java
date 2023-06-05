package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<TestResultDto> findResult(int current, int size, TestResultQueryVo testResultQueryVo) {
        return null;
    }

    /**
     * 修改测试结果
     * @param testResultDto
     * @return
     */
    @Override
    public boolean updateResult(TestResultDto testResultDto) {
        String case_name = testResultDto.getCase_name();
        QueryWrapper<TestResult> wrapper = new QueryWrapper<>();
        wrapper.eq("case_name",case_name);
        TestResult one = this.getOne(wrapper);
        if (null == one) throw new ExceptionEntity(20001,"结果不存在,无法修改......");
        int runNumber = one.getRun_num()+1;
        one.setRun_num(runNumber);
        int runSuccessNumber = one.getRun_success_num()+testResultDto.getRun_success_num();
        one.setRun_success_num(runSuccessNumber);
        int runErrorNUmber = one.getRun_error_num()+testResultDto.getRun_error_num();
        one.setRun_error_num(runErrorNUmber);
        one.setRun_success_rate(Double.valueOf(String.format("%.2f",runSuccessNumber / Double.valueOf(runNumber))));
        this.update(one,wrapper);
        testResultDto.getTestResultInfoList().stream().forEach(item->{
            item.setResult_id(one.getResult_id());
            testResultInfoService.save(item);
        });
        return true;
    }

    /**
     * 添加测试结果
     * @param testResultDto
     * @return
     */
    @Override
    public boolean saveResult(TestResultDto testResultDto) {
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
            testResultInfoService.save(item);
        });
        return true;
    }


}
