package com.zhangjun.quyi.test_task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.test_task.entity.Task;
import com.zhangjun.quyi.test_task.entity.vo.TaskQueryVo;
import com.zhangjun.quyi.test_task.entity.vo.TaskVo;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2022-12-20
 */
public interface TaskService extends IService<Task> {

    /**
     * 添加定时任务
     * @param task
     * @return
     */
    boolean addCronTask(Task task);

    /**
     * 删除定时任务
     * @param cronTaskId
     * @return
     */
    boolean deleteTask(String cronTaskId);

    /**
     * 任务关联用例
     * @param cronTaskId
     * @param caseNames
     * @return
     */
    boolean relationCase(String cronTaskId, List<String> caseNames,String configId) throws JsonProcessingException, Exception;

    /**
     * 执行定时任务
     * @param cronTaskId
     */
    Map<String,Object> runTask(String cronTaskId) throws SchedulerException;

    /**
     * 停止执行
     * @param cronTaskId
     */
    void pauseJob(String cronTaskId,String logId) throws SchedulerException;

    /**
     * 恢复执行
     * @param cronTaskId
     */
    void remuseJob(String cronTaskId,String logId) throws SchedulerException;

    /**
     * 获取所有测试任务
     * @return
     */
    List<Task> selectAllTask();

    /**
     * 分页查询带插件
     * @param current
     * @param size
     * @param taskQueryVo
     * @return
     */
    Map<String,Object> selectAllTaskPage(Integer current, Integer size, TaskQueryVo taskQueryVo);

    /**
     * 获取当前任务已关联的用例
     * @param taskId
     * @return
     */
    List<String> getRelationCase(String taskId);

    /**
     * 停止执行
     * @param
     * @return
     */
    Map<String,Object> stopJob(String cronTaskId,String taskRunLogId) throws SchedulerException;


}
