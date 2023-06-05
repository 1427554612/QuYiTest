package com.zhangjun.quyi.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultModel {
    private String message;      // 返回结果信息
    private Integer code;         // 返回状态码 20000
    private Boolean SUCCESS;     // 是否成功返回
    private Map<String,Object> data = new HashMap<>();  // 返回数据

    private ResultModel(){}

    /**
     * 返回成功，且未装载响应数据的响应Model对象
     * @return
     */
    public static ResultModel ok(){
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(ResultCode.RESULT_CODE_SUCCESS);
        resultModel.setMessage("成功");
        resultModel.setSUCCESS(true);
        return resultModel;
    }
    /**
     * 返回失败，且未装载响应数据的响应Model对象
     * @return
     */
    public static ResultModel error(){
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(ResultCode.RESULT_CODE_ERROR);
        resultModel.setMessage("失败");
        resultModel.setSUCCESS(false);
        return resultModel;
    }


    public ResultModel data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public ResultModel data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    public ResultModel success(Boolean success){
        this.setSUCCESS(success);
        return this;
    }

    public ResultModel message(String message){
        this.setMessage(message);
        return this;
    }

    public ResultModel code(Integer code){
        this.setCode(code);
        return this;
    }

}
