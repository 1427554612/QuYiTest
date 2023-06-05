package com.zhangjun.quyi.test_task.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.zhangjun.quyi.test_task.entity.TaskRunLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@ApiModel
public class TaskVo {

    @ApiModelProperty(value = "任务id")
    private String id;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "cron表达式解释")
    private String cronParse;

    @ApiModelProperty(value = "关联的数据")
    private Map<String,Object> jobs;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "定时类型")
    private String type;

    @ApiModelProperty(value = "定时时间")
    private Integer date;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "最后一次的执行时间")
    private Date lastBeginTime;

    @ApiModelProperty(value = "用例总数")
    private Integer caseCount;

    @ApiModelProperty(value = "运行状态")
    private Integer taskStatus;

    @ApiModelProperty(value = "执行结果集")
    private List<TaskRunLog> taskRunLogs;

}
