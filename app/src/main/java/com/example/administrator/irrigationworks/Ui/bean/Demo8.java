package com.example.administrator.irrigationworks.Ui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6.
 */
public class Demo8 implements Serializable {
    @SerializedName("施工队“三检”制度")
    public String value1;
    @SerializedName("原材料、中间产品见证送检")
    public String value2;

    @SerializedName("混泥土的配合比、拌合物的质量、配料计量偏差符合规范规定")
    public String value3;
    @SerializedName("钢筋的安装质量，钢筋的品种、规格、质量和钢筋的根数符合设计要求，同一截面受力钢筋接头的数量和绑扎接头的搭接长度符合规范规定")
    public String value4;
    @SerializedName("基坑排水措施到位")
    public String value5;
    @SerializedName("砼的自然养护条件")
    public String value6;
    @SerializedName("模板具有足够稳定性、刚度和强度，拼缝严密")
    public String value7;
    @SerializedName("砼表面无蜂窝、麻面现象")
    public String value8;
    @SerializedName("钢砼质评资料")
    public String value9;

    public Demo8() {
    }

    public Demo8(String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.value9 = value9;
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

    public String getValue5() {
        return value5;
    }

    public void setValue5(String value5) {
        this.value5 = value5;
    }

    public String getValue6() {
        return value6;
    }

    public void setValue6(String value6) {
        this.value6 = value6;
    }

    public String getValue7() {
        return value7;
    }

    public void setValue7(String value7) {
        this.value7 = value7;
    }

    public String getValue8() {
        return value8;
    }

    public void setValue8(String value8) {
        this.value8 = value8;
    }

    public String getValue9() {
        return value9;
    }

    public void setValue9(String value9) {
        this.value9 = value9;
    }

    @Override
    public String toString() {
        return "Demo8{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", value3='" + value3 + '\'' +
                ", value4='" + value4 + '\'' +
                ", value5='" + value5 + '\'' +
                ", value6='" + value6 + '\'' +
                ", value7='" + value7 + '\'' +
                ", value8='" + value8 + '\'' +
                ", value9='" + value9 + '\'' +
                '}';
    }
}
