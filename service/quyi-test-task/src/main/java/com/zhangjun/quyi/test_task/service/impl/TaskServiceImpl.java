package com.zhangjun.quyi.test_task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.test_task.entity.Task;
import com.zhangjun.quyi.test_task.entity.TaskCase;
import com.zhangjun.quyi.test_task.entity.TaskRunLog;
import com.zhangjun.quyi.test_task.entity.vo.TaskQueryVo;
import com.zhangjun.quyi.test_task.entity.vo.TaskVo;
import com.zhangjun.quyi.test_task.jobs.SimpleJob;
import com.zhangjun.quyi.test_task.mapper.CronTaskMapper;
import com.zhangjun.quyi.test_task.service.TaskCaseService;
import com.zhangjun.quyi.test_task.service.TaskRunLogService;
import com.zhangjun.quyi.test_task.service.TaskService;
import com.zhangjun.quyi.utils.JsonUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class TaskServiceImpl extends ServiceImpl<CronTaskMapper, Task> implements TaskService {

    private static String JOB_DETAIL_GROUP_NAME = "test";
    @Autowired
    private TaskCaseService taskCaseService;

    @Autowired
    private TaskRunLogService taskRunLogService;

    @Autowired
    private Scheduler scheduler;

    private static Logger logger  = LoggerFactory.getLogger(TaskServiceImpl.class);

    /**
     * 执行测试任务
     * @param cronTaskId
     * @throws SchedulerException
     */
    @Override
    public Map<String,Object> runTask(String cronTaskId) throws SchedulerException {
        Task task = this.getById(cronTaskId);
        String type = task.getType();
        // 构建数据
        JobKey jobKey = new JobKey(task.getName(),JOB_DETAIL_GROUP_NAME);
        if(scheduler.checkExists(jobKey)) scheduler.deleteJob(jobKey);
        JobDetail jobDetail = null;
        Trigger trigger = null;
        jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity(jobKey).build();   // 构建任务
        // 添加数据
        jobDetail.getJobDataMap().put("task",task);
        if (type.equals("cron")){
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron()); // 构建执行器
            trigger = TriggerBuilder.
                    newTrigger().
                    withIdentity(JOB_DETAIL_GROUP_NAME,JOB_DETAIL_GROUP_NAME).
                    withSchedule(cronScheduleBuilder).
                    build();  // 构建触发器
        }else {
            //简单触发器
            trigger = TriggerBuilder.newTrigger().
                    withIdentity(JOB_DETAIL_GROUP_NAME,JOB_DETAIL_GROUP_NAME).
                    startNow().
                    withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(task.getDate()).repeatForever()).
                    build();
        }
        System.out.println("trigger = " +trigger);
        // 插入运行日志
        TaskRunLog taskRunLog = new TaskRunLog();
        taskRunLog.setTaskId(cronTaskId);
        taskRunLog.setBeginTime(new Date());
        // 设置状态为执行中
        taskRunLog.setRunStatus(1);
        taskRunLogService.save(taskRunLog);
        // 获取最新插入的那条数据
        QueryWrapper<TaskRunLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("begin_time");
        queryWrapper.last("limit 1");
        TaskRunLog resultTask = taskRunLogService.list(queryWrapper).get(0);

        scheduler.scheduleJob(jobDetail,trigger);  // 启动任务
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("taskRunLog",resultTask);
        resultMap.put("task",task);
        return resultMap;
    }

    /**
     * 停止执行任务
     * @param cronTaskId
     * @throws SchedulerException
     */
    @Override
    public void pauseJob(String cronTaskId,String logId) throws SchedulerException {
        Task task = this.getById(cronTaskId);
        TaskRunLog taskRunLog = taskRunLogService.getById(logId);
        if (taskRunLog.getRunStatus()==2) throw new ExceptionEntity(20001,"该任务已暂停....");
        taskRunLog.setRunStatus(2);
        taskRunLog.setEndTime(null);
        taskRunLogService.updateById(taskRunLog);
        JobKey jobKey = JobKey.jobKey(task.getName(), "test");
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复执行任务
     * @param cronTaskId
     * @throws SchedulerException
     */
    @Override
    public void remuseJob(String cronTaskId,String logId) throws SchedulerException {
        Task task = this.getById(cronTaskId);
        TaskRunLog taskRunLog = taskRunLogService.getById(logId);
        if (taskRunLog.getRunStatus()!= 2) throw new ExceptionEntity(20001,"当前任务状态不是暂停中");
        taskRunLog.setRunStatus(1);
        taskRunLogService.updateById(taskRunLog);
        JobKey jobKey = JobKey.jobKey(task.getName(), "test");
        scheduler.resumeJob(jobKey);
    }

    /**
     * 获取所有测试任务
     * @return
     */
    @Override
    public List<Task> selectAllTask() {
        List<Task> list = this.list(null);
        return list;
    }

    /**
     * 分页查询带插件
     * @param current
     * @param size
     * @param taskQueryVo
     * @return
     */
    @Override
    public Map<String,Object> selectAllTaskPage(Integer current, Integer size, TaskQueryVo taskQueryVo) {
        Page<Task> page = new Page<>(current,size);
        String taskId = taskQueryVo.getTaskId();
        String name = taskQueryVo.getName();
        String type = taskQueryVo.getType();
        String startLastBeginTime = taskQueryVo.getStartLastBeginTime();
        String endLastBeginTime = taskQueryVo.getEndLastBeginTime();
        QueryWrapper wrapper = new QueryWrapper<TaskVo>();
        if(!StringUtils.isEmpty(taskId)) wrapper.like("id",taskId);
        if (!StringUtils.isEmpty(name)) wrapper.like("name",name);
        if (!StringUtils.isEmpty(type)) wrapper.eq("type",type);
        if (!StringUtils.isEmpty(startLastBeginTime)) wrapper.le("create_time",startLastBeginTime);
        if (!StringUtils.isEmpty(endLastBeginTime)) wrapper.ge("create_time",endLastBeginTime);
        IPage<Task> resultPage = this.page(page, wrapper);
        List<Task> resultList = resultPage.getRecords();
        List<TaskVo> taskVos = taskToTaskVo(resultList);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("total",resultPage.getTotal());
        resultMap.put("list",taskVos);
        return resultMap;
    }


    /**
     * 获取当前任务已关联的用例
     * @param taskId
     * @return
     */
    @Override
    public List<String> getRelationCase(String taskId) {
        QueryWrapper<TaskCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        List<TaskCase> list = taskCaseService.list(queryWrapper);
        List<String> caseIds = new ArrayList<>();
        list.stream().forEach(item -> caseIds.add(item.getCaseId()));
        return caseIds;
    }

    /**
     * 停止定时任务
     * @param cronTaskId
     * @throws SchedulerException
     */
    @Override
    public Map<String,Object> stopJob(String cronTaskId,String taskRunLogId) throws SchedulerException {
        // 查询需要停止的任务名称
        Task task = this.getById(cronTaskId);
        TaskRunLog taskRunLog = taskRunLogService.getById(taskRunLogId);
        if (taskRunLog == null) throw new ExceptionEntity(20001,"该任务已停止或未找到...");
        taskRunLog.setEndTime(new Date());
        // 修改任务状态为已终止
        taskRunLog.setRunStatus(3);
        taskRunLogService.update(taskRunLog,null);
        // 获取触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(JOB_DETAIL_GROUP_NAME,JOB_DETAIL_GROUP_NAME);
        // 获取任务名称和任务组
        JobKey jobKey = JobKey.jobKey(task.getName(), JOB_DETAIL_GROUP_NAME);
        // 执行器中查找触发器
        Trigger trigger =  scheduler.getTrigger(triggerKey);
        logger.info("触发器 ： " + trigger);
        if (trigger == null) throw new ExceptionEntity(20001,"该任务已停止或未找到...");
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(jobKey);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("task",task);
        resultMap.put("taskRunLog",taskRunLog);
        return resultMap;
    }


    /**
     * 添加定时任务
     * @param task
     * @return
     */
    @Override
    public Task addCronTask(Task task) {
        QueryWrapper<Task> cronTaskQueryWrapper = new QueryWrapper<>();
        cronTaskQueryWrapper.eq("name", task.getName());
        if (this.save(task)==false) throw new ExceptionEntity(20001,"定时任务已存在");
        Task resultTask = (Task) this.getOne(cronTaskQueryWrapper);
        return resultTask;
    }

    /**
     * 删除定时任务
     * @param cronTaskId
     * @return
     */
    @Override
    public boolean deleteTask(String cronTaskId) {
        return this.removeById(cronTaskId);
    }


    /**
     * 任务关联用例
     * @param taskId
     * @param caseNames
     * @return
     */
    @Override
    public boolean relationCase(String taskId, List<String> caseNames,String configId) throws Exception {
        logger.info("当前定时任务id:"+ taskId);
        logger.info("关联配置id：" + configId);
        logger.info("关联用例列表：" + JsonUtil.objectMapper.writeValueAsString(caseNames));
        Task task = this.getById(taskId);
        task.setId(taskId);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("configId",configId);
        dataMap.put(HttpConstant.RESPONSE_STR_LIST,caseNames);
        task.setJobs(dataMap);
        if (null == task) throw new ExceptionEntity(20001,"找不到对应定时任务....");
        return this.update(task, null);
    }


    /**
     * 私有方法、task对象转换taskVo对象
     * @param list
     * @return
     */
    private List<TaskVo> taskToTaskVo(List<Task> list){
        ArrayList<TaskVo> resultList = new ArrayList<>();
        list.stream().forEach(task -> {
            // 1、拷贝基础属性到vo类
            TaskVo taskVo = new TaskVo();
            BeanUtils.copyProperties(task,taskVo);
            // 2、设置用例总数
            List<String> caseList = (List<String>)task.getJobs().get(HttpConstant.RESPONSE_STR_LIST);
            taskVo.setCaseCount(caseList.size());
            // 3、设置最后一次任务执行时间
            QueryWrapper<TaskRunLog> beginTimeWrapper = new QueryWrapper<>();
            beginTimeWrapper.eq("task_id",task.getId());
            beginTimeWrapper.orderByDesc("begin_time");
            beginTimeWrapper.last("limit 10");
            List<TaskRunLog> taskRunLogs = null;
            taskRunLogs = taskRunLogService.list(beginTimeWrapper);
            if (taskRunLogs.size()!=0){
                // 4 设置相关时间
                taskVo.setBeginTime(taskRunLogs.get(0).getBeginTime());
                taskVo.setLastBeginTime(taskRunLogs.get(0).getBeginTime());
                taskVo.setEndTime(taskRunLogs.get(0).getEndTime());
                // 5、设置执行状态
                taskVo.setTaskStatus(taskRunLogs.get(0).getRunStatus());
                // 4、设置结果集合
                taskVo.setTaskRunLogs(taskRunLogs);
            }
            else {
                ArrayList<TaskRunLog> objects = new ArrayList<>();
                TaskRunLog taskRunLog = new TaskRunLog();
                taskRunLog.setRunStatus(3);
                objects.add(taskRunLog);
                taskVo.setTaskRunLogs(objects);
            }
            resultList.add(taskVo);
        });
        return resultList;
    }


}
