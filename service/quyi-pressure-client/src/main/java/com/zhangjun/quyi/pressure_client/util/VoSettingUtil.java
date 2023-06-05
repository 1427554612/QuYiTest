package com.zhangjun.quyi.pressure_client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.pressure_client.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_client.entity.vo.ApiResultCountVo;
import com.zhangjun.quyi.pressure_client.entity.vo.PressureCountVo;
import com.zhangjun.quyi.utils.JsonUtil;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class VoSettingUtil {

    /**
     * 返回host汇总对象
     * @param hostRunVos
     * @param requestParamEntity
     * @return
     */
    public static PressureCountVo settingPressureCountVo(List<String> hostRunVos, RequestParamEntity requestParamEntity) throws JsonProcessingException {
        PressureCountVo pressureCountVo = new PressureCountVo();
        int pressureServerCount = 0;           // 所有服务、所有接口的总请求数量
        for(int i = 0;i<hostRunVos.size();++i){
            JsonNode apiRunListNode = JsonUtil.objectMapper.readTree(hostRunVos.get(i)).get("hostRunVo").get("apiRunVoList");
            if (apiRunListNode.isArray()){
                for (JsonNode jsonNode : apiRunListNode) {
                    pressureServerCount += Integer.parseInt(jsonNode.get("requestNumber").asText());
                }
            }
        }
        pressureCountVo.setPressureServerCount(pressureServerCount);
        BeanUtils.copyProperties(requestParamEntity,pressureCountVo);
        pressureCountVo.setHostCount(hostRunVos.size());
        pressureCountVo.setApiResultCountVos(settingApiResultCountVo(hostRunVos));
        pressureCountVo.setHostResult(hostRunVos);
        System.out.println("pressureCountVo = " + JsonUtil.objectMapper.writeValueAsString(pressureCountVo));
        return pressureCountVo;
    }

    /**
     * 返回apiRunVo对象
     * @param
     * @return
     * @throws Exception
     */
    private static List<ApiResultCountVo> settingApiResultCountVo(List<String> hostRunVos) throws JsonProcessingException {
        List<ApiResultCountVo> apiResultCountVos = new ArrayList<>();
        List<JsonNode> ApiRunList = new ArrayList<>();
        Set<String> apiNameSet = new HashSet<>();
        for (int i = 0;i<hostRunVos.size();++i) {
            JsonNode hostRunVoNode = JsonUtil.objectMapper.readTree(hostRunVos.get(i));
            JsonNode apiRunVoListNode = hostRunVoNode.get("hostRunVo").get("apiRunVoList");
            for (JsonNode apiRunVoNode : apiRunVoListNode) {
                ApiRunList.add(apiRunVoNode);
                apiNameSet.add(apiRunVoNode.get("apiName").asText());
            }
        }
        List<Map<String, Object>> maps = apiGroup(ApiRunList, apiNameSet);
        maps.stream().forEach(map->{
            ApiResultCountVo apiResultCountVo = new ApiResultCountVo();
            List<JsonNode> apiVoListJson = (List<JsonNode>) map.get("data");
            long maxRunTime = apiVoListJson.get(0).get("maxRunTime").asLong();
            long minRunTime = apiVoListJson.get(0).get("minRunTime").asLong();
            long avgRunTime;
            long avgRunTimeCount = 0l;
            double tps;
            double errorRate;
            double tpsCount = 0;
            double errorCount = 0;
            for (int i = 0;i<apiVoListJson.size();++i){
                maxRunTime = apiVoListJson.get(i).get("maxRunTime").asLong() > maxRunTime ? apiVoListJson.get(i).get("maxRunTime").asLong() : maxRunTime;
                minRunTime = apiVoListJson.get(i).get("minRunTime").asLong() < minRunTime ? apiVoListJson.get(i).get("minRunTime").asLong() : minRunTime;
                avgRunTimeCount+= apiVoListJson.get(i).get("avgRunTime").asLong();
                tpsCount+= apiVoListJson.get(i).get("tps").asDouble();
                errorCount+= apiVoListJson.get(i).get("errorRate").asDouble();
            }
            avgRunTime = avgRunTimeCount / apiVoListJson.size();
            tps  = tpsCount / apiVoListJson.size();
            errorRate = errorCount / apiVoListJson.size();
            apiResultCountVo.setApiName((String) map.get("apiName"));
            apiResultCountVo.setMaxRunTime(maxRunTime);
            apiResultCountVo.setMinRunTime(minRunTime);
            apiResultCountVo.setAvgRunTime(avgRunTime);
            apiResultCountVo.setTps(tps);
            apiResultCountVo.setErrorRate(errorRate);
            apiResultCountVos.add(apiResultCountVo);
        });

        System.out.println("apiResultCountVos：" + JsonUtil.objectMapper.writeValueAsString(apiResultCountVos));
        return apiResultCountVos;
    }

    /**
     * 接口分组
     * @param ApiRunList
     * @param apiNameSet
     * @return
     */
    private static List<Map<String,Object>> apiGroup(List<JsonNode> ApiRunList,Set<String> apiNameSet){
        List<Map<String,Object>> ApiMapList = new ArrayList<>();
        apiNameSet.stream().forEach(apiName ->{
            Map<String,Object> apiGroupMap = new HashMap<>();
            List<JsonNode> nameList = new ArrayList<>();
            for (JsonNode jsonNode : ApiRunList) {
                if (jsonNode.get("apiName").asText().equals(apiName)){
                    apiGroupMap.put("apiName",jsonNode.get("apiName").textValue());
                    nameList.add(jsonNode);
                }
                apiGroupMap.put("data",nameList);
            }
            ApiMapList.add(apiGroupMap);
        });
        return ApiMapList;
    }

}
