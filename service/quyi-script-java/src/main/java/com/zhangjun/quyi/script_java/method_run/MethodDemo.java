package com.zhangjun.quyi.script_java.method_run;

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;

public class MethodDemo implements FunctionMethod{

    @Override
    public void show() {
        System.out.println("helloworld");
    }

    public  static void ddd() {
        System.out.println("helloworld");
    }

    /**
     * 需要传递函数式接口
     * @param functionMethod
     */
    static void demo(FunctionMethod functionMethod){
        functionMethod.show();
    }

    public static void main(String[] args) {
        demo(MethodDemo::ddd);
    }
}
