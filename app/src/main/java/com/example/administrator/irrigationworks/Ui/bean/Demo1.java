package com.example.administrator.irrigationworks.Ui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
public class Demo1 implements Serializable {
    @SerializedName("监理人员到位")
    public String value1;

    @SerializedName("施工人员到位")
    public String value2;

    @SerializedName("资料同步情况")
    public String value3;
    @SerializedName("健全安全生产宣传教育制度")
    public Object value4;
    @SerializedName("健全安全生产宣传检查制度")
    public String value5;
    @SerializedName("定期召开安全生产会议")
    public String value6;
    @SerializedName("安全生产设施")
    public String value7;
    @SerializedName("安全生产管理人员、特种人员持证上岗")
    public String value8;
    @SerializedName("应急预案编制及演练")
    public String value9;
    @SerializedName("安全资料报送、收集、归档情况")
    public String value10;
    @SerializedName("及时进行安全隐患整改")
    public String value11;
    @SerializedName("工人工资支付公示情况")
    public String value12;

    public Demo1() {
    }

    public Demo1(String value1, String value2, String value3, Object value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, String value12) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.value9 = value9;
        this.value10 = value10;
        this.value11 = value11;
        this.value12 = value12;
    }

    @Override
    public String toString() {
        return "Demo1{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", value3='" + value3 + '\'' +
                ", value4=" + value4 +
                ", value5='" + value5 + '\'' +
                ", value6='" + value6 + '\'' +
                ", value7='" + value7 + '\'' +
                ", value8='" + value8 + '\'' +
                ", value9='" + value9 + '\'' +
                ", value10='" + value10 + '\'' +
                ", value11='" + value11 + '\'' +
                ", value12='" + value12 + '\'' +
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

    public Object getValue4() {
        return value4;
    }

    public void setValue4(Object value4) {
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

    public String getValue10() {
        return value10;
    }

    public void setValue10(String value10) {
        this.value10 = value10;
    }

    public String getValue11() {
        return value11;
    }

    public void setValue11(String value11) {
        this.value11 = value11;
    }

    public String getValue12() {
        return value12;
    }

    public void setValue12(String value12) {
        this.value12 = value12;
    }
}
