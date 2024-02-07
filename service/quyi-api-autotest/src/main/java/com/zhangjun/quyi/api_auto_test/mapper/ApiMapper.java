package com.zhangjun.quyi.api_auto_test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjun.quyi.api_auto_test.entity.ApiEntity;
import com.zhangjun.quyi.api_auto_test.entity.ModuleEntity;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiMapper extends BaseMapper<ApiEntity> {
}
