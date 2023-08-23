package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.resultVo.DataTree;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.test_result.api.TestConfigApi;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.TestResultTempInfo;
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
    public List<TestResult> findResult() {
        List<TestResult> TestResults = this.list();
        List<TestResult> testResultDtos = new ArrayList<>();
        TestResults.stream().forEach( item ->{
            TestResult testResultDto = new TestResult();
            BeanUtils.copyProperties(item,testResultDto);
            String result_id = testResultDto.getResultId();
            QueryWrapper<TestResultInfo> qw = new QueryWrapper<>();
            qw.eq(HttpConstant.API_STR_RESULT_ID,result_id);
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
        if (!StringUtils.isEmpty(testResultQueryVo.getResult_id()))queryWrapper.eq(HttpConstant.API_STR_RESULT_ID,testResultQueryVo.getResult_id());
        if (!StringUtils.isEmpty(testResultQueryVo.getCase_name()))queryWrapper.eq(HttpConstant.API_STR_CASE_NAME,testResultQueryVo.getCase_name());
        if (!StringUtils.isEmpty(testResultQueryVo.getCase_type()))queryWrapper.eq(HttpConstant.API_STR_CASE_TYPE,testResultQueryVo.getCase_type());
        Page<TestResult> resultPage = this.page(page,queryWrapper);
        return resultPage;
    }


    /**
     * 修改测试结果
     * @return
     */
    @Override
    public boolean updateResult(TestResult testResult,String configId) throws Exception {
//        String case_name = testResultDto.getCase_name();
//        QueryWrapper<TestResult> wrapper = new QueryWrapper<>();
//        wrapper.eq(HttpConstant.API_STR_CASE_NAME,case_name);
//        TestResult one = this.getOne(wrapper);
//        if (null == one) throw new ExceptionEntity(20001,"结果不存在,无法修改......");
//        // 先将新的数据插入从表
//        testResultDto.getTestResultInfoList().stream().forEach(item->{
//            item.setResult_id(one.getResult_id());
//            item.setPlatform_id(configId);
//            // 数据插入详情表
//            testResultInfoService.save(item);
//            // 查询添加的详情表数据
//            QueryWrapper<TestResultInfo> resultInfoQueryWrapper = new QueryWrapper<>();
//            resultInfoQueryWrapper.eq(HttpConstant.API_STR_RESULT_ID,item.getResult_id())
//                    .eq(HttpConstant.API_STR_PLATFORM_ID,item.getPlatform_id())
//                    .eq(HttpConstant.API_STR_RUN_BEGIN_TIME,item.getRun_begin_time())
//                    .eq(HttpConstant.API_STR_RUN_END_TIME,item.getRun_end_time())
//                    .eq(HttpConstant.API_STR_RUN_TIME,item.getRun_time());
//            TestResultInfo idTestResult = testResultInfoService.getOne(resultInfoQueryWrapper);
//            // 查询临时表是否存在对应数据
//            QueryWrapper<TestResultTempInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq(HttpConstant.API_STR_RESULT_ID,idTestResult.getResult_id());
//            TestResultTempInfo queryTempResultInfo = testResultTempInfoService.getOne(queryWrapper);
//            BeanUtils.copyProperties(idTestResult,queryTempResultInfo);
//            // 删除这条记录
//            QueryWrapper<TestResultTempInfo> updateWrapper = new QueryWrapper<>();
//            updateWrapper.eq(HttpConstant.API_STR_RESULT_ID,idTestResult.getResult_id());
//            testResultTempInfoService.remove(updateWrapper);
//
//            // 再次插入这条记录
//            testResultTempInfoService.save(queryTempResultInfo);
//        });
//        QueryWrapper<TestResultInfo> testResultInfoQueryWrapperSuccess = new QueryWrapper<>();
//        QueryWrapper<TestResultInfo> testResultInfoQueryError = new QueryWrapper<>();
//
//        // 统计成功总数和失败总数
//        testResultInfoQueryWrapperSuccess.eq(HttpConstant.API_STR_RESULT_ID,one.getResult_id());
//        testResultInfoQueryWrapperSuccess.eq(HttpConstant.API_STR_RUN_RESULT,1);
//        int successCount = testResultInfoService.count(testResultInfoQueryWrapperSuccess);
//        testResultInfoQueryError.eq(HttpConstant.API_STR_RESULT_ID,one.getResult_id());
//        testResultInfoQueryError.eq(HttpConstant.API_STR_RUN_RESULT,0);
//        int errorCount = testResultInfoService.count(testResultInfoQueryError);
//
//        // 查询最近一条状态
//        QueryWrapper<TestResultInfo> testResultInfoQueryWrapper1 = new QueryWrapper<>();
//        testResultInfoQueryWrapper1.eq(HttpConstant.API_STR_RESULT_ID,one.getResult_id());
//        testResultInfoQueryWrapper1.orderByDesc(HttpConstant.API_STR_RUN_BEGIN_TIME);
//        testResultInfoQueryWrapper1.last("limit 1");
//        List<TestResultInfo> TestResultInfo = testResultInfoService.list(testResultInfoQueryWrapper1);
//        com.zhangjun.quyi.test_result.entity.TestResultInfo testResultInfo = TestResultInfo.get(0);
//
//        // 设置成功率
//        one.setRun_num(successCount + errorCount);
//        one.setRun_success_num(successCount);
//        one.setRun_error_num(errorCount);
//        one.setLast_run_date(testResultInfo.getRun_begin_time());
//        one.setLast_run_result(testResultInfo.isRun_result());
//        one.setLast_run_time(testResultInfo.getRun_time());
//
//        // 设置最近执行平台
//        // 设置最近一次执行的平台
//        String platform_id = testResultInfo.getPlatform_id();
//        ResultModel resultModel = testConfigApi.selectConfigById(platform_id);
//        String configName = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(resultModel))
//                .get(HttpConstant.RESPONSE_STR_DATA)
//                .get("testConfig")
//                .get("configName").asText();
//        one.setLast_run_platform(configName);
//        Double aDouble = Double.valueOf(String.format("%.2f", successCount / Double.valueOf(one.getRun_num())));
//        one.setRun_success_rate( (aDouble <= 1 ||aDouble<=1.0  ? (aDouble*100) : aDouble));
//        return this.update(one,wrapper);
        return false;
    }

    /**
     * 添加测试结果
     * @return
     */
    @Override
    public boolean saveResult(String configId,TestResult testResult) throws JsonProcessingException {
       QueryWrapper<TestResult> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq(HttpConstant.API_STR_CASE_NAME,testResult.getCaseName());
       TestResult resultTestResult = this.getOne(queryWrapper);
       if (null != resultTestResult) throw new ExceptionEntity(20001,"用例已存在，无法添加结果......");
        return this.save(testResult);
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


    /**
     * 结果id查询出所有结果详情
     * @param resultId：结果id
     * @param sort：排序方式 1、正序、2、倒叙
     * @return
     */
    @Override
    public List<TestResultInfo> findResultInfoList(String resultId, Integer sort) {
        QueryWrapper<TestResultInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(HttpConstant.API_STR_RESULT_ID,resultId);
        if (sort==1)queryWrapper.orderByAsc(HttpConstant.API_STR_RUN_BEGIN_TIME);
        else queryWrapper.orderByDesc(HttpConstant.API_STR_RUN_BEGIN_TIME);
        return testResultInfoService.list(queryWrapper);
    }


}
