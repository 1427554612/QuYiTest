package com.zhangjun.quyi.script_java.script_sty.janino_class_demo;

import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;

import java.util.ArrayList;
import java.util.List;


/**
 * janino问题:
 *      1、不支持泛型:List<T> list = new ArrayList<>();
 *         使用简易方法：List list = new ArrayList();
 *      2、不支持lambda表达式、采用匿名内部类方式实现
 */
public class JaninoDemo {

    /**
     * 前端传递过来的基础脚本,我们不知道是否有参数已经返回值,
     * 判断四种类型:
     *      1 无参无返回值
     *      2 无参有返回值
     *      3 有参无返回值
     *      4 有参有返回值
     * @param script
     * @return
     */
    static String baseScript(String script){
        if (script.contains("return ")){

        }
        return null;
    }

    /**
     * 无参调用
     * @throws Exception
     */
    static void notParams() throws Exception {
        String content="import com.zhangjun.quyi.script_java.script_sty.janino_class_demo.provader.ClassProvader;" +
                "new ClassProvader().noParams();";
        IScriptEvaluator evaluator = new ScriptEvaluator();
        evaluator.cook(content);
        evaluator.evaluate(null);
    }

    /**
     * 直接执行代码定义的脚本(脚本化执行)
     * @throws Exception
     */
    static void runScritp() throws Exception {
        ScriptEvaluator se = new ScriptEvaluator();
        se.cook(
                ""
                        + "static void method1() {\n"
                        + "    System.out.println(\"run in method1()\");\n"
                        + "}\n"
                        + "\n"
                        + "static void method2() {\n"
                        + "    System.out.println(\"run in method2()\");\n"
                        + "}\n"
                        + "\n"
                        + "method1();\n"
                        + "method2();\n"
                        + "\n"

        );
        se.evaluate(null);
    }

    /**
     * 调用方法带参数
     * @throws Exception
     */
    static void runHasParams() throws Exception{
        ScriptEvaluator se = new ScriptEvaluator();
        se.setParameters(new String[] { "name", "i" }, new Class[] { String.class, int.class });
        String content="import com.zhangjun.quyi.script_java.script_sty.janino_class_demo.provader.ClassProvader;" +
                "new ClassProvader().hasParams(name,i);";
        se.cook(content);
        se.evaluate(new Object[]{"zhangjun",22});
    }

    /**
     * 带返回值
     * @throws Exception
     */
    static void getReturn() throws Exception {
        try {
            IScriptEvaluator se = new ScriptEvaluator();
            se.setReturnType(String.class);
            se.cook("import com.zhangjun.quyi.script_java.script_sty.janino_class_demo.provader.ClassProvader;"
                    + "return new ClassProvader().returnStr();");
            Object res = se.evaluate(null);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
//        notParams();
//        runScritp();
        runHasParams();
        getReturn();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("helloworld");
                }
            }).start();
        }
    }
}
