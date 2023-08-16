package com.zhangjun.quyi.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.org.apache.regexp.internal.RE;
import com.zhangjun.quyi.constans.PressureConstant;
import com.zhangjun.quyi.entity.RequestParamEntity;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RequestUtil {

    public static OkHttpClient client = null;
    private static final Integer CLIENT_TIME_OUT = 90;
    private static final String PROXY_IP = "127.0.0.1"; // 代理ip地址
    private static final Integer PROXY_PORT = 7890;

    /**
     * 设置client对象
     * @param maxRequest
     * @param maxConnections
     */
    public static OkHttpClient setOkhttpClient(Integer maxRequest, Integer maxConnections){
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        if (client == null){
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(maxRequest);   // 设置最大请求数
            dispatcher.setMaxRequestsPerHost(maxRequest);  // 设置最大主机数
            RequestUtil.client =  new OkHttpClient.Builder()
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .connectionPool(new ConnectionPool(maxConnections,10000, TimeUnit.SECONDS)) // 设置连接池连接数量
                    .dispatcher(new Dispatcher())
                    .connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_IP, PROXY_PORT)))   // 使用系统代理
                    .build();
        }
        return RequestUtil.client;
    }


    /**
     * 获取请求对象
     * @param entity：主体参数
     * @param obj：请求体
     * @param url：映射地址
     * @param requestType：请求类型
     * @return：请求对象
     * @throws JsonProcessingException
     */
    public static Request getRequest(String url, RequestParamEntity entity, Object obj, String requestType, Map<String,String> headers) throws JsonProcessingException {
        Request request = null;
        if (headers!= null){
            if (requestType.equals(PressureConstant.REQUEST_TYPE_POST)){
                okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse(PressureConstant.REQUEST_PARAMS_TYPE_APPLICATION_JSON),
                        JsonUtil.objectMapper.writeValueAsString(obj)
                );
                if (!url.contains("admin")){
                    request = new Request.Builder().url(entity.getRequestUrl()+url)
                            .addHeader(PressureConstant.HEADER_USER_AGENT,PressureConstant.HEADER_USER_AGENT_VALUE)
                            .method(PressureConstant.REQUEST_TYPE_POST,body).build();
                }
                else {
                    request = new Request.Builder().url(url)
                            .addHeader(PressureConstant.HEADER_USER_AGENT,PressureConstant.HEADER_USER_AGENT_VALUE)
                            .addHeader("cookie",headers.get("cookie"))
                            .method(PressureConstant.REQUEST_TYPE_POST,body).build();
                }
            }
        }
        else {
            if (requestType.equals(PressureConstant.REQUEST_TYPE_POST)){
                okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse(PressureConstant.REQUEST_PARAMS_TYPE_APPLICATION_JSON),
                        JsonUtil.objectMapper.writeValueAsString(obj)
                );
                if (!url.contains("admin")){
                    request = new Request.Builder().url(entity.getRequestUrl()+url)
                            .addHeader(PressureConstant.HEADER_USER_AGENT,PressureConstant.HEADER_USER_AGENT_VALUE)
                            .method(PressureConstant.REQUEST_TYPE_POST,body).build();
                }
                else {
                    request = new Request.Builder().url(url)
                            .addHeader(PressureConstant.HEADER_USER_AGENT,PressureConstant.HEADER_USER_AGENT_VALUE)
                            .method(PressureConstant.REQUEST_TYPE_POST,body).build();
                }
            }
        }
        return request;
    }


    /**
     * 发送post请求
     * @param ip
     * @param requestBody
     * @return
     */
    public static String sendPost(String ip,String requestBody) throws IOException {
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),requestBody);
        Request request = new Request.Builder().url(ip)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .method("POST",body).build();
        String response = RequestUtil.client.newCall(request).execute().body().string();
        return response;
    }

    /**
     * 基础发送请求
     * @param ip
     * @param requestType
     * @param requestBody
     * @return
     */
    public static String sendRequest(String ip,String requestType,Map<String,Object> requestBody,Map<String,Object> headers) throws IOException {
        return sendRequest(ip,requestType,JsonUtil.objectMapper.writeValueAsString(requestBody),headers);
    }

    /**
     * 基础发送请求
     * @param ip
     * @param requestType
     * @param requestBody
     * @param headers
     * @return
     * @throws IOException
     */
    public static String sendRequest(String ip,String requestType,String requestBody,Map<String,Object> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(ip);
        builder.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36");
        String responseBody = null;
        if (null != headers){
            for (Map.Entry<String, Object> stringObjectEntry : headers.entrySet()) {
                String key = stringObjectEntry.getKey();
                builder.addHeader(key, (String) stringObjectEntry.getValue());
            }
        }
        // 构建post请求
        if (requestType.equals("POST")){
            okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),requestBody);
            Request postRequest = builder.method("POST", body).build();
            responseBody = RequestUtil.client.newCall(postRequest).execute().body().string();

            // 构建get请求
        }else if (requestType.equals("GET")){
            Request getRequest = builder.build();
            responseBody = RequestUtil.client.newCall(getRequest).execute().body().string();
        }else if (requestType.equals("DELETE")){

        }else {

        }
        return responseBody;
    }

    /**
     * 请求方法
     * @param ip
     * @param requestType
     * @param requestBody
     * @param headers
     * @return
     * @throws IOException
     */
    public static Object[] sendingRequest(String ip,String requestType,String requestBody,Map<String,Object> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(ip);
        Response response = null;
        Request request = null;
        // 请求和响应
        Object[] netObjects = new Object[2];
        if (null != headers) {
            for (Map.Entry<String, Object> stringObjectEntry : headers.entrySet()) {
                String key = stringObjectEntry.getKey();
                builder.addHeader(key, (String) stringObjectEntry.getValue());
            }
        }
        // 构建post请求
        if (requestType.equals("POST")) {
            okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody);
            request = builder.method("POST", body).build();
            response = RequestUtil.client.newCall(request).execute();

            // 构建get请求
        } else if (requestType.equals("GET")) {
            request = builder.build();
            response = RequestUtil.client.newCall(request).execute();
        } else if (requestType.equals("DELETE")) {

        } else {

        }
        netObjects[0] = request;
        netObjects[1] = response;
        return netObjects;
    }



}
