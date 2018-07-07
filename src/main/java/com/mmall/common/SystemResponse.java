package com.mmall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;


@JsonSerialize
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SystemResponse<T> implements Serializable {

    private int status;

    private String msg;

    private T data;

    private SystemResponse(int status) {
        this.status = status;
    }

    private SystemResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private SystemResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private SystemResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    /**
     * 创建SystemResponse对象并返回
     * @param status
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> SystemResponse<T> getSystemResponse(int status, String msg, T data) {
        return new SystemResponse<>(status, msg, data);
    }

    public static <T> SystemResponse<T> getSuccessResponse(String msg) {
        return new SystemResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> SystemResponse<T> getSuccessResponse(String msg, T data) {
        return new SystemResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> SystemResponse<T> getErrorResponse(String msg) {
        return new SystemResponse<>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> SystemResponse<T> getErrorResponse(String msg, T data) {
        return new SystemResponse<>(ResponseCode.ERROR.getCode(), msg, data);
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
