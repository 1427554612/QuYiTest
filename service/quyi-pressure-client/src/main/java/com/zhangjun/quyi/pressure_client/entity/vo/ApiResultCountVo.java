package com.zhangjun.quyi.pressure_client.entity.vo;
import lombok.Data;


@Data
public class ApiResultCountVo {
    private String apiName;
    private double errorRate;
    private long maxRunTime;
    private long minRunTime;
    private long avgRunTime;
    private double tps;
}
