package com.zhangjun.quyi.test_result.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.resultVo.DataTree;
import com.zhangjun.quyi.test_result.entity.TestResult;
import com.zhangjun.quyi.test_result.entity.dto.TestResultDto;
import com.zhangjun.quyi.test_result.entity.vo.TestResultQueryVo;

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
    List<TestResultDto> findResult();

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
    boolean updateResult(TestResultDto testResultDtom,String configId) throws Exception;

    /**
     * 添加结果
     * @param testResultDto
     * @return
     */
    boolean saveResult(String configId ,TestResultDto testResultDto) throws Exception;
}
