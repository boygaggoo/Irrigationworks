package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by Administrator on 2017/9/12.
 */
public class ChechWordBean {
    private String hasCheck;
    private String ym;
    private String noCheck;

    public ChechWordBean(String hasCheck, String ym, String noCheck) {
        this.hasCheck = hasCheck;
        this.ym = ym;
        this.noCheck = noCheck;
    }

    public ChechWordBean() {
    }

    @Override
    public String toString() {
        return "ChechWordBean{" +
                "hasCheck='" + hasCheck + '\'' +
                ", ym='" + ym + '\'' +
                ", noCheck='" + noCheck + '\'' +
                '}';
    }

    public String getHasCheck() {
        return hasCheck;
    }

    public void setHasCheck(String hasCheck) {
        this.hasCheck = hasCheck;
    }

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public String getNoCheck() {
        return noCheck;
    }

    public void setNoCheck(String noCheck) {
        this.noCheck = noCheck;
    }
}
