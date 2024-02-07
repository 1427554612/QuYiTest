package com.zhangjun.quyi.api_auto_test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.quyi.api_auto_test.entity.ApiEntity;
import com.zhangjun.quyi.api_auto_test.entity.ModuleEntity;
import com.zhangjun.quyi.api_auto_test.mapper.ApiMapper;
import com.zhangjun.quyi.api_auto_test.mapper.ExportModuleMapper;
import com.zhangjun.quyi.api_auto_test.service.ApiService;
import com.zhangjun.quyi.api_auto_test.service.ExportModuleService;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, ApiEntity> implements ApiService {
}
