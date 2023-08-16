package com.zhangjun.quyi.api_auto_test.api_core.handler;

import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGetting;
import com.zhangjun.quyi.api_auto_test.api_core.components.get.ParamsGettingFactory;
import com.zhangjun.quyi.api_auto_test.api_core.components.set.DivResponseParamsSetting;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.service.impl.ApiAutoTestServiceImpl;
import com.zhangjun.quyi.api_auto_test.util.JavaJaninoUtil;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.service_base.websocket.WebSocketServer;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 核心控制器，控制自动化程序执行
 */
public class ApiRunHandler {

    // 默认请求数量
    public static final Integer DEFAULT_REQUEST_NUMBER = 10;
    private static Logger logger  = LoggerFactory.getLogger(ApiRunHandler.class);

    /**
     * 1、初始化请求池
     */
    static {
        RequestUtil.setOkhttpClient(ApiRunHandler.DEFAULT_REQUEST_NUMBER,ApiRunHandler.DEFAULT_REQUEST_NUMBER);
    }

    /**
     * 执行方法
     * @param caseList：勾选的执行用例列表
     * @param configId：关联配置id
     */
    public static void runApi(List<ApiTestCaseEntity> caseList,String configId) throws Exception {
        // 1、循环遍历传递过来的所有用例
        List<ApiTestCaseEntity> skipCaseList = new ArrayList<>();         // 跳过执行的用例列表
        for (ApiTestCaseEntity apiTestCaseEntity : caseList) {

            // 1.1、过滤所有不执行的用例
            if (apiTestCaseEntity.getIsRun().equals("否") || apiTestCaseEntity.getIsRun().equals("N")){
                skipCaseList.add(apiTestCaseEntity);
                continue;
            }
            System.out.println("前置脚本如下：" + apiTestCaseEntity.getBeforeScript());
            // 1.2、处理前置脚本
            if (null!=apiTestCaseEntity.getBeforeScript()){
                Object o = JavaJaninoUtil.runScript(apiTestCaseEntity.getBeforeScript());
                System.out.println("脚本执行结果如下：" + o);
            }

            // 1.3、获取参数化,替换原本api对象

            if (ParamsGetting.ifNeedGetParams(apiTestCaseEntity)){
                List<ParamsGetting> paramsGettings = ParamsGettingFactory.buildGettingObj(apiTestCaseEntity);
                logger.debug("参数处理器数量：" + paramsGettings.size());
                for (ParamsGetting paramsGetting : paramsGettings) {
                    apiTestCaseEntity = paramsGetting.getParams(DivResponseParamsSetting.ApiParamsEntitys,apiTestCaseEntity);
                }
            }
            System.out.println("替换后的数据：" + apiTestCaseEntity);
            // 推送消息
            WebSocketServer.sendInfo(JsonUtil.objectMapper.writeValueAsString(apiTestCaseEntity), HttpConstant.CONNECTION_SID);
        }

        // 最终阶段：销毁对象&资源
        destroys();

    }

    /**
     * 销毁
     */
    private static void destroys(){
        // 销毁参数获取对象处理类
        ParamsGettingFactory.paramsGettings.clear();
    }
}
