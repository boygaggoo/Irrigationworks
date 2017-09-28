package com.example.administrator.irrigationworks.Ui.litepal;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 获取工地的信息
 * Created by Administrator on 2017/9/5.
 */
public class ConstructionDataLitepal extends DataSupport {
    private String code;
    private String message;
    private String result;

    public ConstructionDataLitepal() {
    }

    public ConstructionDataLitepal(String code, String message, String result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "ConstructionDataLitepal{" +
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
