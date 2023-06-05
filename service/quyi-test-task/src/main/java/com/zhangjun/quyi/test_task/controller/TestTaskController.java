package com.zhangjun.quyi.test_task.controller;


import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.test_task.api.ApiAutoTestApi;
import com.zhangjun.quyi.test_task.entity.Task;
import com.zhangjun.quyi.test_task.entity.vo.TaskQueryVo;
import com.zhangjun.quyi.test_task.entity.vo.TaskVo;
import com.zhangjun.quyi.test_task.service.TaskRunLogService;
import com.zhangjun.quyi.test_task.service.TaskService;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/test_task")
@Api(description = "测试任务管理")
public class TestTaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRunLogService taskRunLogService;

    @Autowired
    private ApiAutoTestApi apiAutoTestApi;

    /**
     * 添加定时任务
     * @param task
     * @return
     */
    @PostMapping("/addTask")
    @ApiOperation(value = "添加定时任务")
    public ResultModel addTask(@ApiParam(name = "task",value = "任务对象")
                                       @RequestBody Task task){
        if (task.equals("cron")) task.setDate(0);
        return taskService.addCronTask(task) == true ? ResultModel.ok() : ResultModel.error();
    }

    /**
     * 修改定时任务
     * @param taskId
     * @param task
     * @return
     */
    @PutMapping("/updateTask/{taskId}")
    @ApiOperation(value = "编辑定时任务")
    public ResultModel updateTask(@ApiParam(name = "taskId",value = "任务id")
                                    @PathVariable String taskId,
                                    @ApiParam(name = "task",value = "task对象")
                                   @RequestBody Task task){
        task.setId(taskId);
        return taskService.update(task, null) == true ? ResultModel.ok() : ResultModel.error();
    }


    /**
     * 关联测试接口
     * @param taskId
     * @return
     */
    @PostMapping("/relationCase/{taskId}/{configId}")
    @ApiOperation(value = "关联测试用例")
    public ResultModel relationCase(@ApiParam(name = "taskId",value = "定时任务id")
                                        @PathVariable(value = "taskId") String taskId,
                                    @ApiParam(name = "configId",value = "配置id")
                                        @PathVariable(value = "configId") String configId,
                                    @ApiParam(name = "caseNames",value = "测试用例名称集")
                                        @RequestBody List<String> caseNames
                                    ) throws Exception {
        return taskService.relationCase(taskId,caseNames,configId) ? ResultModel.ok() : ResultModel.error().message("关联测试用例失败....");
    }

    /**
     * 获取当前任务已关联的用例
     * @param taskId
     * @return
     */
    @GetMapping("/getRelationCase/{taskId}")
    @ApiOperation(value = "获取已关联的用例")
    public ResultModel getRelationCase(@ApiParam(name = "taskId",value = "定时任务id")
                                    @PathVariable String taskId){
        System.out.println("任务id：" + taskId);
        return ResultModel.ok().data("list",taskService.getRelationCase(taskId));
    }


    /**
     * 删除定时任务
     * @param taskId
     * @return
     */
    @DeleteMapping("/deleteCronTask/{taskId}")
    @ApiOperation(value = "删除定时任务")
    public ResultModel deleteTask(@ApiParam(name = "taskId",value = "定时任务id")
                                      @PathVariable String taskId){
        return taskService.deleteTask(taskId) == true ? ResultModel.ok() : ResultModel.error().message("关联删除失败...");
    }

    /**
     * 获取所有定时任务
     * @return
     */
    @GetMapping("/selectAllTask")
    @ApiOperation(value = "获取所有定时任务")
    public ResultModel selectAllTask(){
        List<Task> taskList = taskService.selectAllTask();
        return ResultModel.ok().data("list", taskList);
    }

    /**
     * 分页查询带插件
     * @return
     */
    @PostMapping("/selectAllTask/{current}/{size}")
    @ApiOperation(value = "分页查询带插件")
    public ResultModel selectAllTaskPage(@PathVariable Integer current,
                                         @PathVariable Integer size,
                                         @RequestBody TaskQueryVo taskQueryVo){
        Map<String, Object> resultMap = taskService.selectAllTaskPage(current, size, taskQueryVo);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA, resultMap);
    }

    /**
     * 执行定时任务
     * @return
     */
    @GetMapping("/executeTask/{taskId}")
    @ApiOperation(value = "执行定时任务")
    public ResultModel runTask(@ApiParam(name = "taskId",value = "定时任务id")
                                       @PathVariable String taskId) throws SchedulerException {

        Map<String, Object> resultMap = taskService.runTask(taskId);
        return ResultModel.ok().message("定时任务开启启动成功....").data("task",resultMap.get("task")).data("taskRunLog",resultMap.get("taskRunLog"));
    }

    /**
     * 暂停执行
     * @throws SchedulerException
     */
    @GetMapping("/pauseExecute/{cronTaskId}/{logId}")
    @ApiOperation(value = "暂停执行定时任务")
    public ResultModel pauseJob(@ApiParam(name = "cronTaskId",value = "cron的id")@PathVariable String cronTaskId,@PathVariable String logId) throws SchedulerException {
        taskService.pauseJob(cronTaskId,logId);
        return ResultModel.ok().message("停止执行成功......");
    }

    /**
     * 恢复执行
     * @throws SchedulerException
     */
    @GetMapping("/remuseExecute/{cronTaskId}/{logId}")
    @ApiOperation(value = "恢复执行定时任务")
    public ResultModel remuseJob(@PathVariable String cronTaskId,
                                 @PathVariable String logId) throws SchedulerException {
        taskService.remuseJob(cronTaskId,logId);
        return ResultModel.ok().message("恢复执行成功......");
    }

    /**
     * 停止执行的任务
     */
    @GetMapping("/stopExecute/{cronTaskId}/{taskRunLogId}")
    @ApiOperation(value = "停止执行")
    public ResultModel stopJob(@PathVariable String cronTaskId,@PathVariable String taskRunLogId) throws SchedulerException {
        Map<String, Object> resultMap = taskService.stopJob(cronTaskId, taskRunLogId);
        return ResultModel.ok().message("停止成功...")
                .data("task",resultMap.get("task"))
                .data("taskRunLog",resultMap.get("taskRunLog"));
    }



}

