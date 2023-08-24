package com.zhangjun.quyi.api_auto_test.api_core.components.get.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.handler.ApiRunHandler;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.constans.StrConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 处理请求体中需要的参数
 */
public class PathParamsGetting implements ParamsGetting {

    static {
        LogStringBuilder.logger = LoggerFactory.getLogger(PathParamsGetting.class);
    }

    /**
     * 处理请求地址
     * @param sources
     * @param target
     * @return
     */
    @Override
    public ApiTestCaseEntity getParams(List<ApiParamsEntity> sources, ApiTestCaseEntity target) {
        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + target.getCaseName() + StrConstant.SYMBOL_COMMA + PathParamsGetting.class.getName() + LogStringBuilder.PARAMS_HANDLE_RUN);
        String finalPath = RexUtils.getByCollection(target.getApiPath(), sources);
        target.setApiPath(finalPath);
        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + target.getCaseName() + StrConstant.SYMBOL_COMMA +  " replace data：" + target.getApiPath());
        return target;
    }


}
