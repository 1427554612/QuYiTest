package com.zhangjun.quyi.api_auto_test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.mapper.ParamMapper;
import com.zhangjun.quyi.api_auto_test.service.ParamService;
import com.zhangjun.quyi.constans.SqlConstant;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class ParamServiceImpl  extends ServiceImpl<ParamMapper, ApiParamsEntity> implements ParamService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加自定义参数
     * @param apiParamsEntity
     * @return
     */
    @Override
    @CachePut(value = "test::param",key = "#result.paramId",cacheManager = "cacheManagerPermanent")
    public ApiParamsEntity saveParam(ApiParamsEntity apiParamsEntity) {
        QueryWrapper<ApiParamsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("param_name",apiParamsEntity.getParamName());
        List<ApiParamsEntity> list = this.list(queryWrapper);
        if (list.size()>0) throw new ExceptionEntity(20001,"该名称的参数在数据库中已存在、无法继续添加....");
        this.save(apiParamsEntity);
        QueryWrapper<ApiParamsEntity> saveWrapper = new QueryWrapper<>();
        saveWrapper.orderByDesc("create_time").last(SqlConstant.SQL_LIMIT_1);
        return this.getOne(saveWrapper);
    }

    /**
     * id不能为空
     * @param paramId
     * @return
     */
    @Override
    @Cacheable(value = "test::param",key = "#root.args[0]",cacheManager = "cacheManagerPermanent")
    public ApiParamsEntity findById(String paramId) {
        return this.getById(paramId);
    }

    /**
     * 根据id修改参数
     * @return
     */
    @Override
    @CachePut(value = "test::param",key = "#result.paramId",cacheManager = "cacheManagerPermanent")
    public ApiParamsEntity putById(ApiParamsEntity apiParamsEntity) throws JsonProcessingException, ParseException {
        Object obj = redisTemplate.opsForValue().get("test::param::" + apiParamsEntity.getParamId());
        JsonNode jsonNode = JsonUtil.objectMapper.readTree(JsonUtil.objectMapper.writeValueAsString(obj));
        if (null == jsonNode)throw new ExceptionEntity(20001,"缓存中无法找到该数据...");
        boolean flag = false;
        // 判断数据是否相同
        if (!apiParamsEntity.getParamName().equals(jsonNode.get("paramName").asText())
                || !apiParamsEntity.getParamEq().equals(jsonNode.get("paramEq").asText())
                || !apiParamsEntity.getParamFrom().equals(jsonNode.get("paramFrom").asText())
                || !apiParamsEntity.getParamValue().equals(jsonNode.get("paramValue").asText())
                || !apiParamsEntity.getCaseName().equals(jsonNode.get("caseName").asText())){
            flag = this.updateById(apiParamsEntity);
        }else throw new ExceptionEntity(20001,"参数数据完全相同，无法修改....");
        Boolean deleteFlag = redisTemplate.delete("test::param::" + apiParamsEntity.getParamId());
        if (!deleteFlag) throw new ExceptionEntity(20001,"参数缓存删除失败....");
        return this.getById(apiParamsEntity.getParamId());
    }
}
