package com.zhangjun.quyi.api_auto_test.api_core.enums;

/**
 * 参数来源枚举
 */
public enum ParamsFromEnum {

    REQUEST_HEADER("requestHeader"),
    REQUEST_BODY("requestBody"),
    RESPONSE_CODE("requestCode"),
    RESPONSE_HEADER("requestHeader"),
    RESPONSE_BODY("requestBody");

    public String value;

    ParamsFromEnum(String value){
        this.value = value;
    }
}
