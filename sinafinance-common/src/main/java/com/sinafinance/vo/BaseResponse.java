package com.sinafinance.vo;

import com.sinafinance.exception.ErrorCode;
import com.sinafinance.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 返回结果
 *
 * @author liuruizhi
 * @since 2018/11/26
 */
public class BaseResponse<T> {

    private String respCode;

    private String respMsg;

    private T result;

    @JsonIgnore
    public boolean isSuccess() {
        return StringUtils.equals("0", this.respCode) || StringUtils.equals("00", this.respCode);
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return this.respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public static <T> BaseResponse<T> newSuccResponse(T result) {
        BaseResponse<T> response = new BaseResponse();
        response.setRespCode("00");
        response.setRespMsg(ErrorCode.SUCCESS.getDesc());
        response.setResult(result);
        return response;
    }

    public static <T> BaseResponse<T> newFailResponse(T result) {
        BaseResponse<T> response = new BaseResponse();
        response.setRespCode(ErrorCode.FAIL.getCode());
        response.setResult(result);
        return response;
    }

    public static <T> BaseResponse<T> newFailResponse(ErrorCode result) {
        BaseResponse<T> response = new BaseResponse();
        response.setRespCode(result.getCode());
        response.setRespMsg(result.getDesc());
        response.setResult(null);
        return response;
    }

    public static <T> BaseResponse<T> newFailResponse(String errorCode, String errorMsg) {
        BaseResponse<T> response = new BaseResponse();
        response.setRespCode(errorCode);
        response.setRespMsg(errorMsg);
        return response;
    }

    public static <T> BaseResponse<T> newResponse(String errorCode, String errorMsg, T result) {
        BaseResponse<T> response = new BaseResponse();
        response.setRespCode(errorCode);
        response.setRespMsg(errorMsg);
        response.setResult(result);
        return response;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String toString() {
        return JsonUtils.toJson(this);
    }

}
