package com.zhangjun.quyi.test_task.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.ArrayTypeHandler;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CronTask对象", description="")
@TableName(autoResultMap = true)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "任务名称")
    @NotNull(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "cron表达式解释")
    private String cronParse;

    @ApiModelProperty(value = "关联工作的数据列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String,Object> jobs;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    @NotNull(message = "创建人不能为空")
    private String createBy;

    @ApiModelProperty(value = "定时类型")
    @NotNull(message = "定时类型不能不指定")
    private String type;

    @ApiModelProperty(value = "定时时间")
    private Integer date;

}
