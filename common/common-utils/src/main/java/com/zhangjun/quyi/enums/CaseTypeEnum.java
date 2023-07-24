package com.zhangjun.quyi.enums;

/**
 * 用例类型美剧
 */
public enum CaseTypeEnum {

    API("api"),
    WEB_UI("web-ui"),
    PHONE_UI("phone-ui"),
    PERFORMANCE("performance");

    public String value;

    private CaseTypeEnum(String value){
        this.value = value;
    }

}
