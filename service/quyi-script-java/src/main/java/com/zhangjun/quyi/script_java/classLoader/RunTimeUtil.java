package com.zhangjun.quyi.script_java.classLoader;

public class RunTimeUtil {

    public static void main(String[] args) {
        long runTime = 129600;
        float sunNumber = runTime/(24*60*60f);
        float time = 0.5f;
        float xiaoshi = (time*24);
        System.out.println("运行几天：" + sunNumber);
        System.out.println("运行小时：" + xiaoshi);
    }
}
