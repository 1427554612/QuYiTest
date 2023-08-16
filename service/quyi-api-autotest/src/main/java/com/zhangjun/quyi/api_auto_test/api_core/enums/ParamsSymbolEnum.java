package com.zhangjun.quyi.api_auto_test.api_core.enums;

/**
 * 参数化判断枚举
 */
public enum ParamsSymbolEnum {

    DIV_PARAMS_SYMBOL("#"),   // 自定义参数
    DUILT_PARAMS_SYMBOL("@"); // 内置参数

    public String symbol;
    private ParamsSymbolEnum(String symbol){
        this.symbol = symbol;
    }
}
