package com.zhangjun.quyi.api_auto_test.api_core.components.get.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 处理请求体中需要的参数
 */
public class PathParamsGetting implements ParamsGetting {

    private static Logger logger  = LoggerFactory.getLogger(PathParamsGetting.class);

    /**
     * 处理请求地址
     * @param sources
     * @param target
     * @return
     */
    @Override
    public ApiTestCaseEntity getParams(List<ApiParamsEntity> sources, ApiTestCaseEntity target) {
        logger.debug(target.getCaseName() + " 开始处理，" + PathParamsGetting.class.getName());
        String finalPath = RexUtils.getByCollection(target.getApiPath(), sources);
        target.setApiPath(finalPath);
        logger.debug(target.getCaseName() + " 替换后的用例对象：" + target);
        return target;
    }


}
