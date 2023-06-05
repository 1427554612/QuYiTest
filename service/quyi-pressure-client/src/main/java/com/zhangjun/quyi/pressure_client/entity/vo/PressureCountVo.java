package com.zhangjun.quyi.pressure_client.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class PressureCountVo {

    private Integer requestNumber;
    private Integer pressureServerCount;
    private String requestUrl;
    private List<String> ips;
    private Integer hostCount;
    private List<ApiResultCountVo> apiResultCountVos;
    private List<String> hostResult;
}
