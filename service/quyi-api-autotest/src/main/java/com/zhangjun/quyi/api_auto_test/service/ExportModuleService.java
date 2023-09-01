package com.zhangjun.quyi.api_auto_test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjun.quyi.api_auto_test.entity.ModuleEntity;

import java.util.List;

/**
 * 导出模块
 */
public interface ExportModuleService extends IService<ModuleEntity> {

    /**
     * 导出所有模块
     * @return
     */
    List<ModuleEntity> export();

    /**
     * 添加导出模块
     * @param moduleEntity
     * @return
     */
    ModuleEntity saveEntity(ModuleEntity moduleEntity);
}
