package com.bfd.common.result.exception;

public class ResultError {
    /**
     * 响应代码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object result;

    public ResultError() {
    }

    public ResultError(Object data, Integer resCode, String resMsg) {
        this.code = resCode;
        this.message = resMsg;
        this.result = data;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    /**
     * 失败
     */
    public static ResultError error(Throwable err, ErrorCodeEnum errorInfo) {
        return new ResultError(err.toString(), errorInfo.getResultCode(), errorInfo.getResultMsg());
    }


}