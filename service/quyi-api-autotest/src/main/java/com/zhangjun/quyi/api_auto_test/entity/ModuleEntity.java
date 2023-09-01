package com.zhangjun.quyi.api_auto_test.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@ToString
@ApiModel("模块实例对象")
// 以resultMap方式返回
@TableName(autoResultMap = true,value = "test_module")
public class ModuleEntity {

    @ApiModelProperty(value = "实例id")
    @TableId(type = IdType.ID_WORKER_STR,value = "class_id")
    private String classId;

    @ApiModelProperty(value = "类名")
    @NotNull(message="类名不允许为空")
    private String className;

    @ApiModelProperty(value = "类说明")
    @NotNull(message="类说明不允许为空")
    private String classMark;

    @ApiModelProperty(value = "属性列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Map<String,Object>> attributes;

    @ApiModelProperty(value = "方法列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Map<String,Object>> methods;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
