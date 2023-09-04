package com.zhangjun.quyi.api_auto_test.api_core.components.param.get.impl;

import com.zhangjun.quyi.api_auto_test.api_core.components.param.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.param.set.ParamsSetting;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsEnums;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.api_core.log.LogStringBuilder;
import com.zhangjun.quyi.api_auto_test.api_core.utils.RexUtils;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.constans.StrConstant;
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
        String apiPath = target.getApiPath();
        String finalPath = "";
        for (ParamsEnums.ParamsSymbolEnum paramsSymbolEnum : ParamsEnums.ParamsSymbolEnum.values()) {
            finalPath = RexUtils.getByCollection(apiPath, ParamsSetting.apiParamsEntitys,paramsSymbolEnum.symbol);
        }
        target.setApiPath(finalPath);
        LogStringBuilder.addLog(LogStringBuilder.CASE_NAME + target.getCaseName() + StrConstant.SYMBOL_COMMA +  " replace data：" + target.getApiPath());
        return target;
    }


}
