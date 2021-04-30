package com.bfd.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "通用响应返回对象")
public class Result<T> {

    @ApiModelProperty(value = "结果数据", position = 0)
    private T data;

    @ApiModelProperty(value = "返回码", position = 1)
    private Integer code;

    @ApiModelProperty(value = "返回信息", position = 2)
    private String msg;


    public Result(T data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //成功
    public static <T> Result<T> succeed(T model) {
        return new Result(model, ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg());
    }

    public static <T> Result<T> succeed(ResultCodeEnum resultCodeEnum) {
        return new Result(null, resultCodeEnum.getCode(), resultCodeEnum.getMsg());
    }

    public static <T> Result<T> succeed(T model, ResultCodeEnum resultCodeEnum) {
        return new Result(model, resultCodeEnum.getCode(), resultCodeEnum.getMsg());
    }


    //失败
    public static <T> Result<T> failed(T model) {
        return new Result(model, ResultCodeEnum.ERROR.getCode(), ResultCodeEnum.ERROR.getMsg());
    }

    public static <T> Result<T> failed(ResultCodeEnum resultCodeEnum) {
        return new Result(null, resultCodeEnum.getCode(), resultCodeEnum.getMsg());
    }

    public static <T> Result<T> failed(T model, ResultCodeEnum resultCodeEnum) {
        return new Result(model, resultCodeEnum.getCode(), resultCodeEnum.getMsg());
    }
}
