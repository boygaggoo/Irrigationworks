package com.example.administrator.irrigationworks.Ui.Retrofit;

import java.io.Serializable;

/**
 * 基类
 * Created by YZBbanban on 2017/9/1.
 */

public class ResultCode<T> implements Serializable {
    private String code;
    private String message;
    private T result;

    public ResultCode() {
    }

    public ResultCode(String code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
