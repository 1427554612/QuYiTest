package com.zhangjun.quyi.api_auto_test.api_core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.api_core.components.param.set.ParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsEnums;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.utils.JsonUtil;

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
     * @param source:替换的目标源
     * @param paramsEntityList：匹配的数据列表
     * @param paramTag：匹配的表示
     * @return
     */
    public static String getByCollection(String source, List<ApiParamsEntity> paramsEntityList,String paramTag){
        System.out.println("paramTag = " + paramTag);
        String target = source;
        while (true){
            // 系统参数
            if (paramTag.equals(ParamsEnums.ParamsSymbolEnum.SYSTEM_PARAMS_SYMBOL.symbol)){
                if (!target.contains(paramTag) && !target.equals("")){
                    return target;
                }
                target = getBySystem(paramTag+ "(.*?)"+paramTag,target,true);
                System.out.println("target = " + target);
            }
            // 非系统参数
            else {
                if (!target.contains(paramTag) && !target.equals("")){
                    return target;
                }
                target = forReplaceOnApi(paramTag+ "(.*?)"+paramTag,target,paramsEntityList,paramTag);
            }
        }
    }

    /**
     * 递归替换所有
     * @param source
     * @param paramsEntityList
     * @return
     */
    private static String forReplaceOnApi(String rex ,String source, List<ApiParamsEntity> paramsEntityList,String paramTag){
        String target = "";
        String byRex = "";
        for (ApiParamsEntity apiParamsEntity : paramsEntityList) {
            // 系统参数
            if (paramTag.equals(ParamsEnums.ParamsSymbolEnum.SYSTEM_PARAMS_SYMBOL.symbol)){

            }
            // 非系统参数
            else {
                byRex = getByRex(rex, source);
                // 找到匹配的对象
                if (byRex.equals(apiParamsEntity.getParamName())){
                    target = source.replace(paramTag+byRex+paramTag,apiParamsEntity.getParamValue());
                }
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
    public static String getBySystem(String rex,String source,boolean isReplace){
        System.out.println("rex = " + rex);
        System.out.println("source = " + source);
        Pattern compile = Pattern.compile(rex);
        Matcher matcher = compile.matcher(source);
        String text = null;
        String newText = null;
        while (matcher.find()) {
            text = matcher.group(1); // 第一个组的匹配部分
            System.out.println("text = " + text);
            if (isReplace){
                if (text.equals(ParamsEnums.SystemParamsTypeEnum.THREAD_MAME.type)) {
                    newText = matcher.replaceFirst(Thread.currentThread().getName().replaceAll("-",""));
                    return newText;
                }
                else if (text.equals(ParamsEnums.SystemParamsTypeEnum.UUID.type)) {
                    newText = matcher.replaceFirst(UUID.randomUUID().toString());
                    return newText;
                }
                else if (text.equals(ParamsEnums.SystemParamsTypeEnum.TIME.type)) {
                    newText = matcher.replaceFirst(String.valueOf(System.currentTimeMillis()));
                    return newText;
                }
            }
        }
        return newText;
    }



    public static void main(String[] args) throws JsonProcessingException {
        ApiParamsEntity apiParamsEntity = new ApiParamsEntity("1562362546","div","_id","responseBody","configId\\\":(.*?)\\\"","1692499320026271745");
        ApiParamsEntity apiParamsEntity2 = new ApiParamsEntity("1562362547","login","name","responseBody","configId\\\":(.*?)\\\"","login_success");
        ParamsSetting.apiParamsEntitys.add(apiParamsEntity);
        ParamsSetting.apiParamsEntitys.add(apiParamsEntity2);
        String source = "/api/test/#_id#/%name%/·time·_1@qq.com";
        System.out.println(JsonUtil.objectMapper.writeValueAsString(ParamsSetting.apiParamsEntitys));
        for (ParamsEnums.ParamsSymbolEnum paramsSymbolEnum : ParamsEnums.ParamsSymbolEnum.values()) {
            source = RexUtils.getByCollection(source, ParamsSetting.apiParamsEntitys,paramsSymbolEnum.symbol);
        }
        System.out.println(source);

    }

}
