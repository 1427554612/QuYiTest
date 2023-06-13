package com.zhangjun.quyi.currency_test.utils;

import java.util.ArrayList;

/**
 * 单例集合
 * @param <T>
 */
public class ParamsList<T> extends ArrayList<T> {

    private static ParamsList paramsList = new ParamsList<>();

    private ParamsList(){};

    public static <T>ParamsList<T> getParamsList(){
        if (paramsList!=null) return ParamsList.paramsList;
        else return new ParamsList<>();
    }
}
