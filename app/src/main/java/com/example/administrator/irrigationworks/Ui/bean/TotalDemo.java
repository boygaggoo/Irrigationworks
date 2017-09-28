package com.example.administrator.irrigationworks.Ui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
public class TotalDemo implements Serializable {
    @SerializedName("常规项目")
    public Demo1 name1;
    @SerializedName("施工现场")
    public Demo2 name2;
    @SerializedName("施工风、水、电及通讯")
    public Demo3 name3;
    @SerializedName("金属结构、机电设备及安装工程")
    public Demo4 name4;
    @SerializedName("安全防护设施")
    public Demo5 value5;
    @SerializedName("焊接与气割、危险品管理")
    public Demo6 name6;
    @SerializedName("土方")
    public Demo7 name7;
    @SerializedName("钢砼")
    public Demo8 name8;
    @SerializedName("砌体")
    public Demo9 name9;
    @SerializedName("进度检查")
    public Demo10 name10;

    public TotalDemo() {
    }

    public TotalDemo(Demo1 name1, Demo2 name2, Demo3 name3, Demo4 name4, Demo5 value5, Demo6 name6, Demo7 name7, Demo8 name8, Demo9 name9, Demo10 name10) {
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
        this.value5 = value5;
        this.name6 = name6;
        this.name7 = name7;
        this.name8 = name8;
        this.name9 = name9;
        this.name10 = name10;
    }

    @Override
    public String toString() {
        return "TotalDemo{" +
                "name1=" + name1 +
                ", name2=" + name2 +
                ", name3=" + name3 +
                ", name4=" + name4 +
                ", value5=" + value5 +
                ", name6=" + name6 +
                ", name7=" + name7 +
                ", name8=" + name8 +
                ", name9=" + name9 +
                ", name10=" + name10 +
                '}';
    }

    public Demo1 getName1() {
        return name1;
    }

    public void setName1(Demo1 name1) {
        this.name1 = name1;
    }

    public Demo2 getName2() {
        return name2;
    }

    public void setName2(Demo2 name2) {
        this.name2 = name2;
    }

    public Demo3 getName3() {
        return name3;
    }

    public void setName3(Demo3 name3) {
        this.name3 = name3;
    }

    public Demo4 getName4() {
        return name4;
    }

    public void setName4(Demo4 name4) {
        this.name4 = name4;
    }

    public Demo5 getValue5() {
        return value5;
    }

    public void setValue5(Demo5 value5) {
        this.value5 = value5;
    }

    public Demo6 getName6() {
        return name6;
    }

    public void setName6(Demo6 name6) {
        this.name6 = name6;
    }

    public Demo7 getName7() {
        return name7;
    }

    public void setName7(Demo7 name7) {
        this.name7 = name7;
    }

    public Demo8 getName8() {
        return name8;
    }

    public void setName8(Demo8 name8) {
        this.name8 = name8;
    }

    public Demo9 getName9() {
        return name9;
    }

    public void setName9(Demo9 name9) {
        this.name9 = name9;
    }

    public Demo10 getName10() {
        return name10;
    }

    public void setName10(Demo10 name10) {
        this.name10 = name10;
    }
}
