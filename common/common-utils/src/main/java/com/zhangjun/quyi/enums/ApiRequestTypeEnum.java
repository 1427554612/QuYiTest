package com.zhangjun.quyi.enums;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.utils.JsonUtil;

/**
 * 接口类型枚举
 */
public enum ApiRequestTypeEnum {

    GET("get"),
    POST("post"),
    DELETE("delete"),
    PUT("put"),
    OPTIONS("options");

    public String value;

    private ApiRequestTypeEnum(String value){
        this.value = value;
    }

}
