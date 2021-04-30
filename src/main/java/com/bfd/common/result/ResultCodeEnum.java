package com.bfd.common.result;

public enum ResultCodeEnum {

    //通用
    SUCCESS(0, "操作成功"),
    ADD_SUCCESS(0, "添加成功"),
    UPDATE_SUCCESS(0, "修改成功"),
    DELETE_SUCCESS(0, "删除成功"),
    ERROR(1, "操作失败"),
    NOT_NULL(600, "入参{}不能为NULL"),
    NOT_BLANK(600, "入参{}不能为空"),
    PARAM_ERROR(600, "入参错误{}"),
    EXISTS(650, "{}已存在");


    //角色相关 800

    private Integer code;
    private String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
