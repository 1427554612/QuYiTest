package com.zhangjun.quyi.pressure_server.utlis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.pressure_server.entity.apiEntity.User;
import com.zhangjun.quyi.pressure_server.entity.vo.ApiRunVo;
import com.zhangjun.quyi.pressure_server.entity.vo.ThreadRunVo;
import com.zhangjun.quyi.utils.JsonUtil;

import java.util.*;

public class ParamsUtil {

    /**
     * 设置参数、返回Map<String,String>集合
     * @return
     */
    public static Map<String,String> setParams(String responseBody,User user,String key) throws JsonProcessingException {
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("account",user.getAccount());
        resultMap.put("password",user.getPassword());
        resultMap.put("grecaptcha_token",user.getGrecaptcha_token());
        resultMap.put("_id",setParams(responseBody, key).get("_id"));
        if (user.getPerson()!= null) resultMap.put("parson",user.getPerson());
        return resultMap;
    }


    /**
     * 设置参数、返回Map<String,String>集合
     * @param key
     * @param value
     * @return
     */
    public static Map<String,String> setParams(String key,String value,boolean flag){
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put(key,value);
        return resultMap;
    }

    /**
     * 设置参数、返回Map<String,String>集合
     * data.user.id
     * @param responseBody
     * @param params
     * @return
     */
    public static Map<String,String> setParams(String responseBody,String ... params){
        Map<String,String> resultMap = new HashMap<>();
        Arrays.stream(params).forEach( param ->{
            JsonNode jsonNode = null;
            String name = null;
            try {
                jsonNode = JsonUtil.objectMapper.readTree(responseBody);
                String[] split = param.split("\\.");
                for (String s : split) {
                    name = s;
                    jsonNode = jsonNode.get(s);
                    if (jsonNode.isArray()) jsonNode = jsonNode.get(0);
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            resultMap.put(name,jsonNode.textValue());
        });
        return resultMap;
    }

    /**
     * 获取参数列表
     * @param apiRunVos
     * @param apiName
     * @return
     */
    public static List<Map<String,String>> getParams(List<ApiRunVo> apiRunVos, String apiName,String ... key){
        List<Map<String,String>> paramsList = Collections.synchronizedList(new ArrayList<>());
        Map<String,String> paramsMap  = null;
        for (ApiRunVo apiRunVo : apiRunVos) {
            if (apiName.equals(apiRunVo.getApiName())){
                List<ThreadRunVo> threadRunVoList = apiRunVo.getThreadRunVoList();
                for (ThreadRunVo threadRunVo : threadRunVoList) {
                    paramsMap = new HashMap<>();
                    Map<String, String> params = threadRunVo.getParams();
                    for (String s : key) {
                        paramsMap.put(s,params.get(s));
                    }
                    paramsList.add(paramsMap);
                }
            }
        }
        return paramsList;
    }
}
