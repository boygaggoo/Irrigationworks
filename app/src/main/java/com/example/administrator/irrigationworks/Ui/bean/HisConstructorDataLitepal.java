package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;

/**
 * Created by Administrator on 2017/9/5.
 */
public class HisConstructorDataLitepal {
    private  Constructionbean rg;
    private  String name;
    private  String start_time;
    private  String end_time;
    private InspectorDataLitepal constructor;//施工方
    private String state;

    public HisConstructorDataLitepal() {

    }

    public HisConstructorDataLitepal(Constructionbean rg, String name, String start_time, String end_time, InspectorDataLitepal constructor, String state) {
        this.rg = rg;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.constructor = constructor;
        this.state = state;
    }

    @Override
    public String toString() {
        return "HisConstructorDataLitepal{" +
                "rg=" + rg +
                ", name='" + name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", constructor=" + constructor +
                ", state='" + state + '\'' +
                '}';
    }

    public Constructionbean getRg() {
        return rg;
    }

    public void setRg(Constructionbean rg) {
        this.rg = rg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public InspectorDataLitepal getConstructor() {
        return constructor;
    }

    public void setConstructor(InspectorDataLitepal constructor) {
        this.constructor = constructor;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
