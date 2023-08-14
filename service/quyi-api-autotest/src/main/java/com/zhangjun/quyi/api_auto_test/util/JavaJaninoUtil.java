package com.zhangjun.quyi.api_auto_test.util;

import org.codehaus.janino.ScriptEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * java脚本处理类
 */
public class JavaJaninoUtil {

    /**
     * 前后置脚本枚举
     */
    enum ScriptEnum{
        BEFORE_SCRIPT("beforeScript"),
        AFTER_SCRIPT("afterScript");

        private String str;

        ScriptEnum(String str){
            this.str =str;
        }

    }

    /**
     * key:表示前后置
     * @param script
     * @return
     */
    public static Object runScript(String script) throws Exception {
        if (script.contains("return")) return hasReturnScript(script);
        else notReturnScript(script);
        return null;
    }

    /**
     * 有返回值处理方法
     * @param script
     * @return
     * @throws Exception
     */
    private static Object hasReturnScript(String script)throws Exception {
        ScriptEvaluator se = new ScriptEvaluator();
        se.setReturnType(Object.class);
        se.cook(script);
        return se.evaluate(null);
    }

    /**
     * 无返回值处理方法
     * @param script
     * @throws Exception
     */
    private static void notReturnScript(String script) throws Exception {
        ScriptEvaluator se = new ScriptEvaluator();
        se.cook(script);
        se.evaluate(null);
    }

    public static void main(String[] args) throws Exception {
        String script = "return System.currentTimeMillis();";
        Object o = JavaJaninoUtil.runScript(script);
        System.out.println(o);
    }
}
