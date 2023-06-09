package com.zhangjun.quyi.script_java.script;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderScript {

    /**
     * class加载流程:
     *  1、读取本地class文件加载到内存
     *  2、
     * @param args
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        ClassLoader classLoader = ClassLoaderScript.class.getClassLoader();
        Class<?> demo = classLoader.loadClass("com.zhangjun.quyi.script_java.classfile.Demo");
        System.out.println(demo);
    }
}
