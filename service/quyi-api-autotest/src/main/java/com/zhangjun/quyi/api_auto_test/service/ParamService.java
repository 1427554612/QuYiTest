package com.zhangjun.quyi.api_auto_test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.entity.ModuleEntity;

import java.text.ParseException;

public interface ParamService extends IService<ApiParamsEntity> {

    /**
     * 添加自定义参数
     * @param apiParamsEntity
     * @return
     */
    ApiParamsEntity saveParam(ApiParamsEntity apiParamsEntity);

    /**
     * id不能为空
     * @param paramId
     * @return
     */
    ApiParamsEntity findById(String paramId);

    /**
     * 根据id修改参数
     * @return
     */
    ApiParamsEntity putById(ApiParamsEntity apiParamsEntity) throws JsonProcessingException, ParseException;
}
