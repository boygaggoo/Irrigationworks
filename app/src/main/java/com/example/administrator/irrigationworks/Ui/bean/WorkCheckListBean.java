package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.ConstructorDataLitepal;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6.
 */
public class WorkCheckListBean  {
    private String start_time;
    private int flag;
    private Double lnt;
    private Double lat;
    private String date_time;
    private String rg;
    private WordConstructionData construction;
    private ConstructorDataLitepal user;

    public WorkCheckListBean() {

    }

    public WorkCheckListBean(String start_time, int flag, Double lnt, Double lat, String date_time, String rg, WordConstructionData construction, ConstructorDataLitepal user) {
        this.start_time = start_time;
        this.flag = flag;
        this.lnt = lnt;
        this.lat = lat;
        this.date_time = date_time;
        this.rg = rg;
        this.construction = construction;
        this.user = user;
    }

    @Override
    public String toString() {
        return "WorkCheckListBean{" +
                "start_time='" + start_time + '\'' +
                ", flag=" + flag +
                ", lnt=" + lnt +
                ", lat=" + lat +
                ", date_time='" + date_time + '\'' +
                ", rg='" + rg + '\'' +
                ", construction=" + construction +
                ", user=" + user +
                '}';
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Double getLnt() {
        return lnt;
    }

    public void setLnt(Double lnt) {
        this.lnt = lnt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public WordConstructionData getConstruction() {
        return construction;
    }

    public void setConstruction(WordConstructionData construction) {
        this.construction = construction;
    }

    public ConstructorDataLitepal getUser() {
        return user;
    }

    public void setUser(ConstructorDataLitepal user) {
        this.user = user;
    }
}
