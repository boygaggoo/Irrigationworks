package com.example.administrator.irrigationworks.Ui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6.
 */
public class Demo10 implements Serializable {
    @SerializedName("节点工期实施情况")
    public String value1;

    @SerializedName("重要部位实施情况防洪度汛实施情况")
    public String value2;

    @SerializedName("形象进度")
    public String value3;

    public Demo10() {
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

    public Demo10(String value1, String value2, String value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    @Override
    public String toString() {
        return "Demo10{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", value3='" + value3 + '\'' +
                '}';
    }
}
