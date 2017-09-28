package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.SupervisorDataLitepal;

/**
 * Created by Administrator on 2017/9/6.
 */
public class TaskCourDbbean {
     private String state;
    private InspectorDataLitepal constructor;//施工方
    private SupervisorDataLitepal supervisor;//监理
    private String rg;//施工方
    private String lnt;//
    private String lat;//

    public TaskCourDbbean() {
    }

    public TaskCourDbbean(String state, InspectorDataLitepal constructor, SupervisorDataLitepal supervisor, String rg, String lnt, String lat) {
        this.state = state;
        this.constructor = constructor;
        this.supervisor = supervisor;
        this.rg = rg;
        this.lnt = lnt;
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "TaskCourDbbean{" +
                "state='" + state + '\'' +
                ", constructor=" + constructor +
                ", supervisor=" + supervisor +
                ", rg='" + rg + '\'' +
                ", lnt='" + lnt + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public InspectorDataLitepal getConstructor() {
        return constructor;
    }

    public void setConstructor(InspectorDataLitepal constructor) {
        this.constructor = constructor;
    }

    public SupervisorDataLitepal getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorDataLitepal supervisor) {
        this.supervisor = supervisor;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getLnt() {
        return lnt;
    }

    public void setLnt(String lnt) {
        this.lnt = lnt;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
