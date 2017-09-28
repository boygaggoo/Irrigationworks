package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.ChiefsupervisorDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.PmDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.SupervisorDataLitepal;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/15.
 */
public class ShigongfangistBean {
    private  String name;
    private  String area;//片区
    private  String address;//片区
    private  String start_time;
    private  Double lnt;
    private  Double lat;
    private  String end_time;
    private  String rg;
    private  String constructionid;
    private InspectorDataLitepal constructor;//施工方
    private SupervisorDataLitepal supervisor;//施工方
    private ChiefsupervisorDataLitepal chiefsupervisor;//总监理
    private PmDataLitepal pm;//施工方
    private String state;
    private String[][] coordinate;
    public ShigongfangistBean() {
    }

    public ShigongfangistBean(String name, String area, String address, String start_time, Double lnt, Double lat, String end_time, String rg, String constructionid, InspectorDataLitepal constructor, SupervisorDataLitepal supervisor, ChiefsupervisorDataLitepal chiefsupervisor, PmDataLitepal pm, String state, String[][] coordinate) {
        this.name = name;
        this.area = area;
        this.address = address;
        this.start_time = start_time;
        this.lnt = lnt;
        this.lat = lat;
        this.end_time = end_time;
        this.rg = rg;
        this.constructionid = constructionid;
        this.constructor = constructor;
        this.supervisor = supervisor;
        this.chiefsupervisor = chiefsupervisor;
        this.pm = pm;
        this.state = state;
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "ShigongfangistBean{" +
                "name='" + name + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", start_time='" + start_time + '\'' +
                ", lnt=" + lnt +
                ", lat=" + lat +
                ", end_time='" + end_time + '\'' +
                ", rg='" + rg + '\'' +
                ", constructionid='" + constructionid + '\'' +
                ", constructor=" + constructor +
                ", supervisor=" + supervisor +
                ", chiefsupervisor=" + chiefsupervisor +
                ", pm=" + pm +
                ", state='" + state + '\'' +
                ", coordinate=" + Arrays.toString(coordinate) +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
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

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getConstructionid() {
        return constructionid;
    }

    public void setConstructionid(String constructionid) {
        this.constructionid = constructionid;
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

    public ChiefsupervisorDataLitepal getChiefsupervisor() {
        return chiefsupervisor;
    }

    public void setChiefsupervisor(ChiefsupervisorDataLitepal chiefsupervisor) {
        this.chiefsupervisor = chiefsupervisor;
    }

    public PmDataLitepal getPm() {
        return pm;
    }

    public void setPm(PmDataLitepal pm) {
        this.pm = pm;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String[][] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String[][] coordinate) {
        this.coordinate = coordinate;
    }
}
