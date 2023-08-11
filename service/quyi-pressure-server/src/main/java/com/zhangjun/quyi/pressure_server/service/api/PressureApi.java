package com.zhangjun.quyi.pressure_server.service.api;
import com.zhangjun.quyi.constans.PressureConstant;
import com.zhangjun.quyi.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_server.entity.apiEntity.ExchangeCode;
import com.zhangjun.quyi.pressure_server.entity.apiEntity.User;
import com.zhangjun.quyi.pressure_server.entity.vo.ThreadRunVo;
import com.zhangjun.quyi.pressure_server.utlis.VoSettingUtil;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PressureApi {

    public static Integer amount = 10000;  // 充值金额
    public static final String CODE  = "8A4935504E8068638DAA5F555";   // 兑换码

    /**
     * 注册-不带邀请码
     * @param entity
     * @return
     * @throws IOException
     */
    public static ThreadRunVo registerApi(RequestParamEntity entity) throws IOException {
        String name = ("zhangjun"+System.currentTimeMillis()+Thread.currentThread().getName()+"@phlwin.phlwin").replaceAll("-","");
        String password = "zj123456";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6aGFuZ2p1bi11c2VyIiwiaWF0IjoxNjc4MDkxMTExLCJleHAiOjE2NzgxNzc1MTEsImlkIjoiMTIzIiwibmlja25hbWUiOiJ6aGFuZ2p1biJ9.SOwY5hK0ZxMazva57QBVm_sdR0X0xxSPLHG80FTkFQ8";
        String parent = "643e0ecd5b590c3d40d26cbc";
        User user = new User(name, password, token,parent);
        Request request = RequestUtil.getRequest("/user/register", entity, user, PressureConstant.REQUEST_TYPE_POST,null);
        long st = System.currentTimeMillis();
        Response response = RequestUtil.client.newCall(request).execute();
        long ed = System.currentTimeMillis();
        String bodyString = response.body().string();
        return VoSettingUtil.setThreadRunVo(st, ed, Thread.currentThread().getName(), bodyString, "data._id",null,user,"data._id");
    }


    /**
     * 登录接口
     * @param entity
     */
    public static ThreadRunVo loginApi(RequestParamEntity entity,Map<String,String> paramsMap) throws IOException {
        User user = new User(paramsMap.get("account"),paramsMap.get("password"),paramsMap.get("grecaptcha_token"));
        Request request = RequestUtil.getRequest("/user/login", entity, user, PressureConstant.REQUEST_TYPE_POST,null);
        long st = System.currentTimeMillis();
        Response response = RequestUtil.client.newCall(request).execute();
        long ed = System.currentTimeMillis();
        String bodyString = response.body().string();
        return VoSettingUtil.setThreadRunVo(st,ed,Thread.currentThread().getName(),bodyString,"data.user._id",null,null,"data.token", "data.user._id");
    }

    /**
     * 使用兑换码
     * @param entity
     * @param paramsMap
     * @return
     */
    public static ThreadRunVo userCodeApi(RequestParamEntity entity,Map<String,String> paramsMap) throws IOException {
        ExchangeCode exchangeCode = null;
        Request request = null;
        String bodyString = null;
        long st = 0l;
        long ed = 0l;
        for(int i = 0;i<100;++i){
            // 兑换码
            exchangeCode = new ExchangeCode(CODE,paramsMap.get("token"),paramsMap.get("_id"));
            request = RequestUtil.getRequest("/api/v1/coupon/number/use", entity, exchangeCode, PressureConstant.REQUEST_TYPE_POST,null);
            st = System.currentTimeMillis();
            Response response = RequestUtil.client.newCall(request).execute();
            ed = System.currentTimeMillis();
            bodyString = response.body().string();
            System.out.println("code is use : " + bodyString + ",id is : " + paramsMap.get("_id"));
        }
        // 兑换码
        return VoSettingUtil.setThreadRunVo(st,ed,Thread.currentThread().getName(),bodyString,"code",0,null,"data");
    }


    /**
     * 上报埋点
     * @param entity
     * @return
     * @throws IOException
     */
    public static ThreadRunVo reportApi(RequestParamEntity entity) throws IOException {
        Map<String,String> map = new HashMap<>();
        map.put("click_id","iuVMroVs1pe8Ti+AXh5Hrw==");
        map.put("event_name","EVENT_COMPLETE_REGISTRATION");
        map.put("pixel_id","466432465180831816");
        map.put("click_url","https://mexlucky.pre-release.xyz/?click_id=iuVMroVs1pe8Ti+AXh5Hrw==&pixel_id=466432465180831816&channel=kwaiq&channel_id=414799196844985311");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse(PressureConstant.REQUEST_PARAMS_TYPE_APPLICATION_JSON),
                JsonUtil.objectMapper.writeValueAsString(map)
        );
        Request request = new Request.Builder().url(entity.getRequestUrl()+"/api/v1/burail_report/adsnebula/report")
                .addHeader(PressureConstant.HEADER_USER_AGENT,"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .method("POST",body).build();
        long st = System.currentTimeMillis();
        String responseBody = RequestUtil.client.newCall(request).execute().body().string();
        long ed = System.currentTimeMillis();
        return VoSettingUtil.setThreadRunVo(st,ed,Thread.currentThread().getName(),responseBody,"code",0,null,"code");
    }


    /**
     * 充值
     * @param entity
     * @param paramsMap
     * @return
     */
    public static ThreadRunVo rechargeApi(RequestParamEntity entity,Map<String,String> paramsMap) throws IOException {
        amount = 10000;
        Map<String,Object> map = new HashMap<>();
        map.put("amount",amount);
        map.put("task_id","undefined");
        map.put("token",paramsMap.get("token"));
        map.put("user_id",paramsMap.get("_id"));
        if (entity.getRequestUrl().contains("phlwin") || entity.getRequestUrl().contains("philucky") || entity.getRequestUrl().contains("apanalo")){
            map.put("currency","PHP");
            map.put("email","aaa@163.com");
            map.put("first_name","aaa");
            map.put("last_name","aaa");
            map.put("phone","6666666666");
            map.put("typ","GCASH");
            map.put("amount",amount);
            Map<String,String> methodMap = new HashMap<>();
            methodMap.put("pay_method","electronic_wallet");
            methodMap.put("typ","GCASH");
            map.put("data",methodMap);
        }
        Request request = RequestUtil.getRequest("/user/recharge", entity, map, PressureConstant.REQUEST_TYPE_POST,null);
        long st = System.currentTimeMillis();
        Response response = RequestUtil.client.newCall(request).execute();
        long ed = System.currentTimeMillis();
        String responseBody = response.body().string();
        return VoSettingUtil.setThreadRunVo(st,ed,Thread.currentThread().getName(),responseBody,"code",200,null,"data.order_id");
    }



    /**
     * 投注接口
     * @param entity
     * @param paramsMap
     * @return
     */
    public static ThreadRunVo betApi(RequestParamEntity entity,Map<String,String> paramsMap) throws IOException {
        Map<String,Object> map = new HashMap<>();
        if (entity.getRequestUrl().contains("aajogo")) map.put("bet_amount",5000);
        map.put("token",paramsMap.get("token"));
        map.put("user_id",paramsMap.get("_id"));
        Request request = RequestUtil.getRequest("/game/cryptos/bet", entity, map, PressureConstant.REQUEST_TYPE_POST,null);
        long st = System.currentTimeMillis();
        Response response = RequestUtil.client.newCall(request).execute();
        long ed = System.currentTimeMillis();
        System.out.println(response.code());
        System.out.println(response.body().string());
        return null;
    }


//    public static void main(String[] args) throws IOException {
//        RequestUtil.setOkhttpClient(10000,10000);
//        RequestParamEntity requestParamEntity = new RequestParamEntity();
//        requestParamEntity.setRequestUrl("https://aajogo-api.pre-release.xyz");
//        requestParamEntity.setRequestNumber(1);// 线程用户数
//        requestParamEntity.setIps(Arrays.asList("192.168.5.12:8057"));
//    }



}
