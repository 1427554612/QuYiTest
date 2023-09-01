package com.zhangjun.quyi.script_java.script_sty.classloading_domo;

public class ClassLoadingDemo {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass1 = Class.forName("com.zhangjun.quyi.api_auto_test.api_core.components.param.set.impl.DivResponseParamsSettingImpl");
        System.out.println(aClass1);
    }
}
