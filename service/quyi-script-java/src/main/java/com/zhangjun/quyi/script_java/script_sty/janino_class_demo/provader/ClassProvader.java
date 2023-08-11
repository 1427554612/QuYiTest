package com.zhangjun.quyi.script_java.script_sty.janino_class_demo.provader;

public class ClassProvader {

    public void noParams(){
        System.out.println("hello,world");
    }

    public void hasParams(String name,Integer i){
        System.out.println(name + "is run " + i + "次...");
    }

    public String returnStr(){
        return "returnStr is run...";
    }

    public String returnStrAndP(String name,Integer i){
        return name + "is run " + i + "次...";
    }

    public static void main(String[] args) {
        System.out.println(new ClassProvader().returnStr());
    }

}
