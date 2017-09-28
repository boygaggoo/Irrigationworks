package com.example.administrator.irrigationworks.Ui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6.
 */
public class Demo4 implements Serializable {
    @SerializedName("制造单位的生产许可证或认证证书")
    public String value1;

    @SerializedName("设备进场验收记录")
    public String value2;
    @SerializedName("金属结构、机电设备及安装工程质评资料")
    public String value3;

    public Demo4() {
    }

    @Override
    public String toString() {
        return "Demo4{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", value3='" + value3 + '\'' +
                '}';
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }
}
