package com.zhangjun.quyi.enums;



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
