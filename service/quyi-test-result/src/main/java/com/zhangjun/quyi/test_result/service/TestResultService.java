package com.zhangjun.quyi.test_result.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.resultVo.DataTree;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.TestResultInfo;
import com.zhangjun.quyi.test_result.entity.vo.TestResultQueryVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface TestResultService extends IService<TestResult> {

    /**
     * 查询日志文件树
     * @return
     */
    List<DataTree> findLogTree() throws Exception;


    /**
     * 查询具体日志
     * @param queryMap
     */
    String findLog(Map<String, Object> queryMap) throws Exception;


    /**
     * 查询所有结果以及详情
     * @return
     */
    List<TestResult> findResult();

    /**
     * 条件查询带分页
     * @param current
     * @param size
     * @param testResultQueryVo
     * @return
     */
    Page<TestResult> findResult(int current, int size, TestResultQueryVo testResultQueryVo);


    /**
     * 修改结果
     * @return
     */
    boolean updateResult(TestResult testResult,TestResultInfo testResultInfo) throws Exception;

    /**
     * 添加结果
     * @param testResultDto
     * @return
     */
    boolean saveResult(String configId ,TestResult testResultDto) throws Exception;

    /**
     * 清空所有结果和统计数据
     */
    void deleteAllResult();


    /**
     * 查询该id的数据
     * @return
     */
    TestResult findById(String resultId);
}
