package com.zhangjun.quyi.test_result.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "结果查询vo")
public class TestResultQueryVo {
    @ApiModelProperty(value = "结果id")
    private String result_id;

    @ApiModelProperty(value = "用例名称")
    private String case_name;

    @ApiModelProperty(value = "用例类型")
    private String case_type;

}
