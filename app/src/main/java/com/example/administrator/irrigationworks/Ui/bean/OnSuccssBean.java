package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by Administrator on 2017/8/15.
 */
public class OnSuccssBean {
    private String code;
    private String message;
    private String result;

    public OnSuccssBean() {
    }

    public OnSuccssBean(String code, String message, String result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "OnSuccssBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result='" + result + '\'' +
                '}';
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
