package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.ChiefsupervisorDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.SupervisorDataLitepal;

/**
 * Created by Administrator on 2017/9/13.
 */
public class TotalGeDangerlistBean {
    private String area;
    private String address;
    private String supervisor_institution;
    private String end_time;
    private String start_time;
    private String constructionid;
    private String lnt;
    private String _id;
    private String state;
    private String name;
    private InspectorDataLitepal constructor;
    private SupervisorDataLitepal supervisor;
    private HisPmDataLitepal pm;
    private ChiefsupervisorDataLitepal chiefsupervisor;//总监理

    public TotalGeDangerlistBean() {
    }

    public TotalGeDangerlistBean(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "TotalGeDangerlistBean{" +
                "area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", supervisor_institution='" + supervisor_institution + '\'' +
                ", end_time='" + end_time + '\'' +
                ", start_time='" + start_time + '\'' +
                ", constructionid='" + constructionid + '\'' +
                ", lnt='" + lnt + '\'' +
                ", _id='" + _id + '\'' +
                ", state='" + state + '\'' +
                ", name='" + name + '\'' +
                ", constructor=" + constructor +
                ", supervisor=" + supervisor +
                ", pm=" + pm +
                ", chiefsupervisor=" + chiefsupervisor +
                '}';
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

    public String getSupervisor_institution() {
        return supervisor_institution;
    }

    public void setSupervisor_institution(String supervisor_institution) {
        this.supervisor_institution = supervisor_institution;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getConstructionid() {
        return constructionid;
    }

    public void setConstructionid(String constructionid) {
        this.constructionid = constructionid;
    }

    public String getLnt() {
        return lnt;
    }

    public void setLnt(String lnt) {
        this.lnt = lnt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public HisPmDataLitepal getPm() {
        return pm;
    }

    public void setPm(HisPmDataLitepal pm) {
        this.pm = pm;
    }

    public ChiefsupervisorDataLitepal getChiefsupervisor() {
        return chiefsupervisor;
    }

    public void setChiefsupervisor(ChiefsupervisorDataLitepal chiefsupervisor) {
        this.chiefsupervisor = chiefsupervisor;
    }
}
