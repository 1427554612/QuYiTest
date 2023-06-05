package com.zhangjun.quyi.api_auto_test.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@ApiModel(value = "测试用例对象")
public class ApiTestCaseEntity {

    @ApiModelProperty(value = "用例编号")
    @Excel(name = "用例编号")
    @NotNull(value = "必须填写用例编号")
    private Integer caseNumber;

    @ApiModelProperty(value = "用例名称")
    @Excel(name = "用例名称")
    @NotNull(value = "必须填写用例名称")
    private String caseName;

    @ApiModelProperty(value = "用例标题")
    @Excel(name = "用例标题")
    @NotNull(value = "必须填写用例标题")
    private String caseTitle;

    @ApiModelProperty(value = "接口地址")
    @Excel(name = "接口地址")
    @NotNull(value = "必须填写接口地址")
    private String apiPath;

    @ApiModelProperty(value = "请求方式")
    @Excel(name = "请求方式")
    @NotNull(value = "必须填写请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "主流程api")
    @Excel(name = "主流程api")
    @NotNull(value = "必须填写主流程api")
    private String isMainProcessApi;

    @ApiModelProperty(value = "请求头")
    @Excel(name = "请求头")
    @NotNull(value = "必须填写请求头")
    private Object requestHeaders;

    @ApiModelProperty(value = "请求体")
    @Excel(name = "请求体")
    private Object requestBody;

    @ApiModelProperty(value = "是否执行")
    @Excel(name = "是否执行")
    @NotNull(value = "必须填写是否执行")
    private String isRun;

    @ApiModelProperty(value = "断言内容")
    @Excel(name = "断言内容")
    @NotNull(value = "必须填写断言")
    private Object assertMap;

    @ApiModelProperty(value = "是否参数化")
    @Excel(name = "是否参数化")
    @NotNull(value = "必须填写是否参数化")
    private String isParams;

    @ApiModelProperty(value = "参数化数据")
    @Excel(name = "参数化数据")
    private Object paramList;
}
