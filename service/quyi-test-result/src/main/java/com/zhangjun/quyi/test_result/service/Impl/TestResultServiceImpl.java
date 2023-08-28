package com.zhangjun.quyi.test_result.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.resultVo.DataTree;
import com.zhangjun.quyi.service_base.handler.GlobalExceptionHandler;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.test_result.api.TestConfigApi;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.vo.TestResultQueryVo;
import com.zhangjun.quyi.test_result.mapper.TestResultServiceMapper;
import com.zhangjun.quyi.test_result.service.TestResultInfoService;
import com.zhangjun.quyi.test_result.service.TestResultService;
import com.zhangjun.quyi.test_result.service.TestResultTempInfoService;
import com.zhangjun.quyi.utils.FileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Cacheable(value = "test-result",key = "'log-tree'",cacheManager = "cacheManager3Minute")
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
    @Cacheable(value = "test-result",key = "'log'",cacheManager = "cacheManager1Minute")
    public List<TestResult> findResult() {
        return this.list();
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
    public boolean updateResult(TestResult testResult,TestResultInfo testResultInfo) {
        String resultId =testResult.getResultId();
        testResultInfo.setResultId(testResult.getResultId());
        // 先插入最新结果详情
        testResultInfoService.saveResultInfo(testResultInfo);

        // 设置结果成功总数、失败总数、执行总数
        QueryWrapper<TestResultInfo> successAndErrorNumber = new QueryWrapper<>();
        successAndErrorNumber.eq("result_id",resultId);
        successAndErrorNumber.groupBy("run_result");
        successAndErrorNumber.select("count(*) as 'group',run_result");
        List<Map<String, Object>> groupSelectList = testResultInfoService.listMaps(successAndErrorNumber);
        int number = 0;
        for (Map<String, Object> stringObjectMap : groupSelectList) {
            boolean run_result = (boolean)stringObjectMap.get("run_result");
            int successOrErrorNumber = Integer.valueOf(stringObjectMap.get("group").toString());
            if (run_result) testResult.setRunSuccessNum(successOrErrorNumber);
            else testResult.setRunErrorNum(successOrErrorNumber);
            number+= successOrErrorNumber;
        }
        testResult.setRunNum(number);
        System.out.println("testResult = " + "总数：" + testResult.getRunNum()  + ",成功数：" + testResult.getRunSuccessNum() + ",失败数:" + testResult.getRunErrorNum());

        // 更改最新结果
        return this.updateById(testResult);

    }

    /**
     * 添加测试结果
     * @return
     */
    @Override
    public boolean saveResult(String configId,TestResult testResult){
        // 插入到数据库
        QueryWrapper<TestResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(HttpConstant.API_STR_CASE_NAME,testResult.getCaseName());
        List<TestResult> list = this.list(queryWrapper);
        if (list.size()>0) throw new ExceptionEntity(20001,"用例已存在，无法添加结果......");
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
