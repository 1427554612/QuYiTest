package com.zhangjun.quyi.api_auto_test.api_core.utils;

import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RexUtils {

    /**
     * 解析并返回匹配的字符
     * @param rex
     * @param source
     * @return
     */
    public static String getByRex(String rex,String source){
        Pattern compile = Pattern.compile(rex);
        Matcher matcher = compile.matcher(source);
        String returnText = null;
        while (matcher.find()){
            returnText = matcher.group(1);
            return returnText;
        }
        return returnText;
    }

    /**
     * 从容器中匹配查找到的字符,并替换到源对象中
     * @param source
     * @return
     */
    public static String getByCollection(String source, List<ApiParamsEntity> paramsEntityList){
        String target = source;
        while (true){
            if (!target.contains("#") && !target.equals("")){
                return target;
            }
            target = forReplace(target,paramsEntityList);
        }
    }

    /**
     * 递归替换所有
     * @param source
     * @param paramsEntityList
     * @return
     */
    private static String forReplace(String source, List<ApiParamsEntity> paramsEntityList){
        String target = "";
        String byRex = "";
        for (ApiParamsEntity apiParamsEntity : paramsEntityList) {
            byRex = getByRex("#(.*?)#", source);
            // 找到匹配的对象
            if (byRex.equals(apiParamsEntity.getParamName())){
                target = source.replace("#"+byRex+"#",apiParamsEntity.getParamValue());
            }
        }
        return target;
    }


    /**
     * 内置参数处理
     * @param rex
     * @param source
     * @param isReplace
     * @return
     */
    public static String getByBuilt(String rex,String source,boolean isReplace){
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



    public static void main(String[] args) throws Exception {
//        String body = "{\"message\":\"成功\",\"code\":20000,\"data\":{\"testConfig\":{\"configId\":\"1691052557096472577\",\"configName\":\"wzzss\",\"configData\":{},\"configType\":\"string\",\"createTime\":\"2023-08-14 19:42:02\",\"updateTime\":\"2023-08-14 19:42:02\",\"updateUp\":\"string\",\"configMark\":\"sdasdasd\"}},\"success\":true}\n";
//        String rex = "configId\":\"(.*?)\",\"";
//        String replace = RexUtils.replace(rex, body);
//        System.out.println(replace);
        String source = "/api/demo/#zhangjun1#/#zhangjun2#";
        List<ApiParamsEntity> list = new ArrayList<>();
        for (int i = 0;i<5;++i){
            ApiParamsEntity apiParamsEntity = new ApiParamsEntity();
            apiParamsEntity.setParamName("zhangjun"+i);
            apiParamsEntity.setParamValue("value = " + i);
            list.add(apiParamsEntity);
        }
        System.out.println(getByCollection(source,list));
    }

}
