package com.example.administrator.irrigationworks.Ui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6.
 */
public class Demo9 implements Serializable {
    @SerializedName("料石、块石的质量、规格符合设计要求和规范规定")
    public String value1;

    @SerializedName("砌体的砌筑质量，砌体相互错缝，坐实挤紧，空隙嵌实")
    public String value2;
    @SerializedName("浆砌石的砌筑砂浆饱满密实")
    public String value3;
    @SerializedName("砌体质评资料")
    public String value4;


    public Demo9() {
    }

    public Demo9(String value1, String value2, String value3, String value4) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    @Override
    public String toString() {
        return "Demo9{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", value3='" + value3 + '\'' +
                ", value4='" + value4 + '\'' +
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

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }
}
