package com.zhangjun.quyi.test_task.jobs;

import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.test_task.api.ApiAutoTestApi;
import com.zhangjun.quyi.test_task.entity.Task;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.ResultModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 任务类
 */
@Slf4j
@Component
public class SimpleJob implements Job {

    @Autowired
    private ApiAutoTestApi apiAutoTestApi;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        Task task = (Task)jobExecutionContext.getJobDetail().getJobDataMap().get("task");
        Map<String, Object> jobMap = task.getJobs();
        ResultModel resultModel = apiAutoTestApi.runCase((ArrayList<String>)jobMap.get(HttpConstant.RESPONSE_STR_LIST),(String) jobMap.get("configId"));
        log.info("执行结果如下：" + JsonUtil.objectMapper.writeValueAsString(resultModel));
    }

    /**
     * spring内置定时器、用作定时任务
     *  常用cron：每天12点执行：0 0 12 * * ?
     */
    @Scheduled(cron = "*/5 * * * * ?")
    private void statisticsJob(){
        System.out.println("statisticsJob is run ...");
    }
}
