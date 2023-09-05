package com.zhangjun.quyi.api_auto_test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 参数设置实体类
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="test_params")
@ApiModel(value = "参数实体类")
public class ApiParamsEntity {
    @TableId(type = IdType.ID_WORKER_STR,value = "param_id")
    @ApiModelProperty(value = "参数id")
    String paramId;

    @ApiModelProperty(value = "用例名称,次数为div类型用例，名称都是div")
    String caseName;   // 用例名称，表示这个参数创建于哪个用例

    @ApiModelProperty(value = "参数名称")
    @NotNull(message = "参数名称不能为空")
    @Length(min = 3,max = 30,message = "名称长度在3-30之间")
    String paramName;  // 参数名称，表示其他用例要如何引用此用

    @ApiModelProperty(value = "参数来源")
    @NotNull(message = "参数来源不能为空")
    String paramFrom;  // 参数来源，表示参数从哪里来，requestHeader,requestBody,responseCode,responseHeader,responseBody

    @ApiModelProperty(value = "这个实例匹配的表达式")
    String paramEq;   // 匹配表达式，@xxx@表示内置，$xxx$表示自定义

    @ApiModelProperty(value = "匹配到的真实的值")
    String paramValue; // 匹配到的值

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime; // 创建时间

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date updateTime; // 修改时间
}
