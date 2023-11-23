package com.zhangjun.quyi.performance.entity;

import lombok.Data;

import java.util.Map;

@Data
public class ApiEntity extends Thread{
    private String apiName;
    private String url;
    private String requestBody;
    private Map<String,Object> headers;
    private String responseBody;
    private Map<String,Object> performanceConfig;
    private Map<String,Object> result;
}
