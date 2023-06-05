package com.zhangjun.quyi.pressure_server.utlis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.constans.PressureConstant;
import com.zhangjun.quyi.pressure_server.entity.apiEntity.User;
import com.zhangjun.quyi.pressure_server.entity.vo.ApiRunVo;
import com.zhangjun.quyi.pressure_server.entity.vo.HostRunVo;
import com.zhangjun.quyi.pressure_server.entity.vo.ThreadRunVo;
import com.zhangjun.quyi.utils.DateTimeUtil;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class VoSettingUtil {

    /**
     * 设置ThreadRunVo对象
     * @param st:开始时间戳
     * @param en：结束时间戳
     * @param threadName：线程名称
     * @param responseBody：响应正文
     * @param paramsKeys：需要保存的参数
     * @return：线程执行结果对象
     * @throws JsonProcessingException
     */
    public static ThreadRunVo setThreadRunVo(long st,long en,
                                             String threadName,
                                             String responseBody,
                                             String assertKey,
                                             Object assertValue,
                                             User user,
                                             String ... paramsKeys) throws JsonProcessingException {
        ThreadRunVo threadRunVo = new ThreadRunVo();
        threadRunVo.setThreadStartTimeStamp(st);
        threadRunVo.setThreadEndTimeStamp(en);
        long threadStartTimeStamp = threadRunVo.getThreadStartTimeStamp();
        long threadEndTimeStamp = threadRunVo.getThreadEndTimeStamp();
        threadRunVo.setThreadName(threadName);
        threadRunVo.setThreadStartTime(DateTimeUtil.dateForString(new Date(threadStartTimeStamp)));
        threadRunVo.setThreadEndTime(DateTimeUtil.dateForString(new Date(threadEndTimeStamp)));
        threadRunVo.setThreadRunTime(threadEndTimeStamp - threadStartTimeStamp);
        threadRunVo.setError(
                null == assertValue ?
                        !AssertUtil.assertResponseTextNotIsNull(responseBody,assertKey) :
                        !AssertUtil.assertResponseTextEquals(responseBody,assertKey,assertValue));
        if (!threadRunVo.isError()) {
            if (paramsKeys!=null) threadRunVo.setParams(
                    null == user ? ParamsUtil.setParams(responseBody,paramsKeys) : ParamsUtil.setParams(responseBody,user,paramsKeys[0]));
            else threadRunVo.setParams(null);
        }
        else threadRunVo.setParams(null);
        threadRunVo.setErrorMsg(threadRunVo.isError() ? responseBody : null);
        System.out.println(threadRunVo);
        return threadRunVo;
    }

    /**
     * 设置ApiRunVo对象
     * @param futureList
     * @param apiName
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static ApiRunVo setApiRunVo(List<Future> futureList,String apiName,String type,int requestNumber) throws ExecutionException, InterruptedException, JsonProcessingException {
        ApiRunVo apiRunVo = new ApiRunVo();
        apiRunVo.setRequestNumber(requestNumber);
        long minTime = ((ThreadRunVo)futureList.get(0).get()).getThreadStartTimeStamp();
        long maxTime = ((ThreadRunVo)futureList.get(0).get()).getThreadEndTimeStamp();
        long countTime = 0l;
        long minRunTime = ((ThreadRunVo)futureList.get(0).get()).getThreadRunTime();
        long maxRunTime = minRunTime;
        long errorCount = 0;
        for ( int i = 0 ;i<futureList.size();++i){
            ThreadRunVo threadRunVo = (ThreadRunVo)futureList.get(i).get();
            long threadRunTime = threadRunVo.getThreadRunTime();
            minTime = threadRunVo.getThreadStartTimeStamp()<minTime ? threadRunVo.getThreadStartTimeStamp() : minTime;
            maxTime = threadRunVo.getThreadEndTimeStamp()>maxTime ? threadRunVo.getThreadEndTimeStamp() : maxTime;
            minRunTime = threadRunTime < minRunTime ? threadRunTime: minRunTime;
            maxRunTime = threadRunTime > maxRunTime ? threadRunTime : maxRunTime;
            countTime+=threadRunTime;
            errorCount = threadRunVo.isError() == true ? errorCount+1 : errorCount;
            apiRunVo.getThreadRunVoList().add(threadRunVo);
        }
        apiRunVo.setApiName(apiName);
        apiRunVo.setApiType(type);
        apiRunVo.setApiStartTimeStamp(minTime);
        apiRunVo.setApiEndTimeStamp(maxTime);
        apiRunVo.setApiStartTime(new Date(apiRunVo.getApiStartTimeStamp()));
        apiRunVo.setApiEndTime(new Date(apiRunVo.getApiEndTimeStamp()));
        apiRunVo.setApiRunTime(maxTime - minTime);             // 接口运行时长
        apiRunVo.setAvgRunTime(countTime/futureList.size());   // 接口平均响应时间
        apiRunVo.setMaxRunTime(maxRunTime);
        apiRunVo.setMinRunTime(minRunTime);
        apiRunVo.setTps(Double.parseDouble(String.format(PressureConstant.DOUBLE_STR,futureList.size() / (apiRunVo.getApiRunTime() / 1000f))));
        apiRunVo.setErrorRate(Double.parseDouble(String.format(PressureConstant.DOUBLE_STR,(errorCount /(double)requestNumber) * 100)));
        return apiRunVo;
    }

    /**
     * 设置hostVo
     * @return
     */
    public static HostRunVo setHostVo(List<ApiRunVo> apiRunVoList) throws Exception {
        HostRunVo hostRunVo = new HostRunVo();
        hostRunVo.setAddress(InetAddress.getLocalHost().getHostAddress());
        hostRunVo.setApiRunVoList(apiRunVoList);
        long apiStartTime = apiRunVoList.get(0).getApiStartTimeStamp();
        long apiEndTime = apiRunVoList.get(0).getApiEndTimeStamp();
        int requestCountNumber = 0;
        for (int i = 0;i<apiRunVoList.size();++i){
            ApiRunVo apiRunVo = apiRunVoList.get(i);
            apiStartTime = apiRunVo.getApiStartTimeStamp() < apiStartTime ? apiRunVo.getApiStartTimeStamp() : apiStartTime;
            apiEndTime = apiRunVo.getApiEndTimeStamp() > apiEndTime ? apiRunVo.getApiEndTimeStamp() : apiEndTime;
            requestCountNumber+=apiRunVoList.get(i).getRequestNumber();
        }
        hostRunVo.setHostStartTime(new Date(apiStartTime));
        hostRunVo.setHostEndTime(new Date(apiEndTime));
        hostRunVo.setHostRunTime(apiEndTime -apiStartTime);
        hostRunVo.setRequestNumber(apiRunVoList.get(0).getRequestNumber());
        hostRunVo.setAllApiRequestNumber(requestCountNumber);
        hostRunVo.setPoolThreadNumber(((ThreadPoolExecutor)ThreadPoolUtil.executors).getMaximumPoolSize());
        return hostRunVo;
    }
}
