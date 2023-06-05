package com.zhangjun.quyi.entity;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RequestParamEntity {

    @NotNull(message = "并发数不能为空")
    @Min(value = 1, message = "并发数至少为1")
    @Max(value = 100000, message = "单台负载机最大限制10万请求")
    private Integer requestNumber;

    @NotNull(message = "请求地址不能为空")
    private String requestUrl;

    @NotNull(message = "负载机不能为空")
    private List<String> ips;
}
