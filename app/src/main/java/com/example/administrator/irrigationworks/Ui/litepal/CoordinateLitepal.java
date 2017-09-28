package com.example.administrator.irrigationworks.Ui.litepal;

import org.litepal.crud.DataSupport;

/**
 * 获取工地的坐标
 * Created by Administrator on 2017/9/5.
 */
public class CoordinateLitepal extends DataSupport {
    private String code;

    public CoordinateLitepal() {
    }

    public CoordinateLitepal(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CoordinateLitepal{" +
                "code='" + code + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
