package com.zhangjun.quyi.script_java.regex;

import org.python.antlr.ast.Str;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则提取问题：
 *      1、rex表达式中、不能使用 $ { }  []  () 等做为表达式前后参数、否则提取不了
 */
public class RegexDemo {

    public static String replace(String rex,String source){
        String text = get(rex, source,true);
        while (true){
            if (!text.contains("@")) return text;
            text = get(rex, text, true);
        }
    }

    public static String get(String rex,String source,boolean isReplace){
        Pattern compile = Pattern.compile(rex);
        Matcher matcher = compile.matcher(source);
        String text = null;
        String newText = null;
        while (matcher.find()) {
            text = matcher.group(1); // 第一个组的匹配部分
            if (isReplace){
                if (text.equals("thread")) {
                    newText = matcher.replaceFirst(Thread.currentThread().getName().replaceAll("-",""));
                    return newText;
                }
                else if (text.equals("uuid")) {
                    newText = matcher.replaceFirst(UUID.randomUUID().toString());
                    return newText;
                }
                else if (text.equals("time")) {
                    newText = matcher.replaceFirst(String.valueOf(System.currentTimeMillis()));
                    return newText;
                }
            }
        }
        return newText;
    }

    public static void main(String[] args) {
        String body = "{\n" +
                "  \"configData\": {\"demo\":\"test\"},\n" +
                "  \"configMark\": \"string\",\n" +
                "  \"configName\": \"@thread@_@uuid@_@time@\",\n" +
                "  \"configType\": \"api\",\n" +
                "  \"updateUp\": \"张军\"\n" +
                "}";
        String rex = "@(.*?)@";
        String replace = RegexDemo.replace(rex, body);
        System.out.println(replace);
    }
}
