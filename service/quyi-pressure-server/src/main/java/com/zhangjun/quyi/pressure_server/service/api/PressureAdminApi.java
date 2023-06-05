package com.zhangjun.quyi.pressure_server.service.api;

import com.zhangjun.quyi.constans.PressureConstant;
import com.zhangjun.quyi.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_server.entity.vo.ThreadRunVo;
import com.zhangjun.quyi.pressure_server.utlis.VoSettingUtil;
import com.zhangjun.quyi.utils.DateTimeUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

public class PressureAdminApi {
    private static final String COOKIE =
            "connect.sid=s%3AIwzgYv5Npm8Twg4VwA4WhgdiYMcoRkU-.zOJgEQtQ%2B4oRE9J7IFAMUWkbqZQBb%2FgtlLuokITCVIA; _ga=GA1.1.264852527.1681984016; _tt_enable_cookie=1; _ttp=L6QhGb6eAD9Vr7CXcAWVoelg3g_; _gcl_au=1.1.1576271867.1682059564; _ga_850W33XS49=GS1.1.1682065983.8.1.1682068812.0.0.0; _fbp=fb.1.1682216742407.449953671";


    /**
     * 查询接口
     * @param entity
     * @return
     */
    public static ThreadRunVo listRechargeApi(RequestParamEntity entity, Map<String,String> paramMap) throws IOException {
        Map<String,String> headers = new HashMap<>();
        headers.put("cookie",COOKIE);
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("current",1);
        bodyMap.put("pageSize",20);
        bodyMap.put("user_id",paramMap.get("user_id"));
        bodyMap.put("pos",0);
        bodyMap.put("size",20);
        Map<String,Object> sortMap = new HashMap<>();
        sortMap.put("create_at", -1);
        bodyMap.put("sort",sortMap);
        bodyMap.put("gameId",100);
        Request request = RequestUtil.getRequest(PressureConstant.BASE_ADMIN_URL + "/adminpanel/v2/json/list_recharge", entity, bodyMap, PressureConstant.REQUEST_TYPE_POST, headers);
        long st = System.currentTimeMillis();
        Response response = RequestUtil.client.newCall(request).execute();
        long ed = System.currentTimeMillis();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        return VoSettingUtil.setThreadRunVo(st,ed,Thread.currentThread().getName(),responseBody,"code",0,null,"data.data.trans_id");
    }

    /**
     * 补单接口
     * @param entity
     * @return
     */
    public static ThreadRunVo notifyRecharge(RequestParamEntity entity, Map<String,String> paramsMap) throws IOException {
        Map<String,String> headers = new HashMap<>();
        headers.put("cookie",COOKIE);
        Map<String,Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("orderid",paramsMap.get("trans_id"));
        long transactionId = DateTimeUtil.getTransactionMillisecondId();
        requestBodyMap.put("transaction_id", transactionId);
        requestBodyMap.put("datetime",DateTimeUtil.dateForString(new Date(transactionId)));
        requestBodyMap.put("amount",PressureApi.amount);
        String mappingUrl = PressureConstant.BASE_ADMIN_URL.contains("aajogo")
                || PressureConstant.BASE_ADMIN_URL.contains("betfiery")
                ? "/adminpanel/v2/json/notify_recharge"
                : "/adminpanel/v2/json/pay/notify_recharge";
        Request request = RequestUtil.getRequest(PressureConstant.BASE_ADMIN_URL + mappingUrl, entity, requestBodyMap, PressureConstant.REQUEST_TYPE_POST, headers);
        long st = System.currentTimeMillis();
        Response response = RequestUtil.client.newCall(request).execute();
        long ed = System.currentTimeMillis();
        String responseBody = response.body().string();
        if (PressureConstant.BASE_ADMIN_URL.contains("aajogo")
                || PressureConstant.BASE_ADMIN_URL.contains("betfiery"))
            return VoSettingUtil.setThreadRunVo(st,ed,Thread.currentThread().getName(),responseBody,"message","请求错误：1003,unknown error",null,null);
        return VoSettingUtil.setThreadRunVo(st,ed,Thread.currentThread().getName(),responseBody,"msg","success",null,null);
    }


//    public static void main(String[] args) throws IOException {
//        RequestParamEntity requestParamEntity = new RequestParamEntity();
//        requestParamEntity.setRequestNumber(1);
//        requestParamEntity.setIps(Arrays.asList("192.168.1.11:3000"));
//        requestParamEntity.setRequestUrl("https://philucky-admin.pre-release.xyz");
//        RequestUtil.setOkhttpClient(10000,10000);
//        Map<String,String> map = new HashMap<>();
//        map.put("user_id","6438b8bbc9b6b54008ec1be6");
//        listRechargeApi(requestParamEntity,map);
//    }

}
