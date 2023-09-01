package com.zhangjun.quyi.api_auto_test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.entity.ModuleEntity;
import com.zhangjun.quyi.api_auto_test.mapper.ExportModuleMapper;
import com.zhangjun.quyi.api_auto_test.service.ExportModuleService;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.utils.JsonUtil;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模块导出实现
 */

@Service
public class ExportModuleServiceImpl  extends ServiceImpl<ExportModuleMapper, ModuleEntity> implements ExportModuleService {

    /**
     * 导出所有模块
     * @return
     */
    @Override
    @Cacheable(value = "test::module",key = "'list'",cacheManager = "cacheManager3Minute")
    public List<ModuleEntity> export() {
        //TODO:导出所有模块
        return this.list(null);
    }

    /**
     * 添加模块
     * @param moduleEntity
     * @return
     */
    @Override
    @CachePut(value = "test::module",key = "#result.classId",cacheManager = "cacheManagerPermanent")
    public ModuleEntity saveEntity(ModuleEntity moduleEntity) {
        // 1、判断这个类在数据库是否存在
        QueryWrapper<ModuleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_name",moduleEntity.getClassName());
        List<ModuleEntity> list = this.list(queryWrapper);
        if (list.size()>0)throw new ExceptionEntity(20001,"类在数据库已存在，无法添加此数据....");
        // 2、判断这个类在系统中是否存在
        Class<?> aClass = null;
        try {
            aClass = Class.forName(moduleEntity.getClassName());
        }catch (Exception e){
            throw new ExceptionEntity(20001,"这个类在系统内不存在、无法添加这个类....");
        }
        List<Map<String, Object>> fieIds = Arrays.stream(aClass.getDeclaredFields())
                .map(fieId -> {
                    fieId.setAccessible(true);
                    Map<String, Object> fieIdMap = new HashMap<>();
                    fieIdMap.put("fieIdName", fieId.getName());
                    fieIdMap.put("fieIdType", fieId.getType().getSimpleName());
                    fieIdMap.put("isStatic", fieId.toString().contains("static") ? true : false);
                    return fieIdMap;
                }).collect(Collectors.toList());
        // 4、设置所有方法
        List<Map<String,Object>> methods = Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> !method.getName().equals("main"))
                .map(method -> {
                    Map<String,Object> methodMap = new HashMap<>();
                    methodMap.put("methodName",method.getName());
                    methodMap.put("isStatic", method.toString().contains("static") ? true : false);
                    methodMap.put("returnType",method.getReturnType().getSimpleName());
                    List<Map<String,Object>> params = new ArrayList<>();
                    Arrays.stream(method.getParameters()).forEach(parameter-> {
                        Map<String,Object> paramsMap = new HashMap<>();
                        paramsMap.put("paramName",parameter.getName());
                        paramsMap.put("paramType",parameter.getType().getSimpleName());
                        params.add(paramsMap);
                    });
                    methodMap.put("paramTypes",params);
                    return methodMap;
                }).collect(Collectors.toList());
        moduleEntity.setAttributes(fieIds);
        moduleEntity.setMethods(methods);
        this.save(moduleEntity);
        QueryWrapper<ModuleEntity> resultModuleQuery = new QueryWrapper<>();
        resultModuleQuery.orderByDesc("create_time").last("limit 1");
        ModuleEntity one = this.getOne(resultModuleQuery);
        return one;
    }

}
