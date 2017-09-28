package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.ConstructorDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.SupervisorDataLitepal;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/5.
 */
public class HoistoricaDataLitepal extends DataSupport {
    private String are;
    private String adress;
    private String state;
    private String lnt;
    private String lat;
    private String name;
    private String end_time;
    private String constructionid;
    private String start_time;

    private int flag;
    private HisInspectorDataLitepal constructor;
    private String rg;//_id更换成rg
    private InspectorDataLitepal inspector;
    private HisPmDataLitepal pm;
    private SupervisorDataLitepal supervisor;//施工方
    private boolean expand;
    public HoistoricaDataLitepal() {

    }

    public HoistoricaDataLitepal(String are, String adress, String state, String lnt, String lat, String name, String end_time, String constructionid, String start_time, int flag, HisInspectorDataLitepal constructor, String rg, InspectorDataLitepal inspector, HisPmDataLitepal pm, SupervisorDataLitepal supervisor, boolean expand) {
        this.are = are;
        this.adress = adress;
        this.state = state;
        this.lnt = lnt;
        this.lat = lat;
        this.name = name;
        this.end_time = end_time;
        this.constructionid = constructionid;
        this.start_time = start_time;
        this.flag = flag;
        this.constructor = constructor;
        this.rg = rg;
        this.inspector = inspector;
        this.pm = pm;
        this.supervisor = supervisor;
        this.expand = expand;
    }

    @Override
    public String toString() {
        return "HoistoricaDataLitepal{" +
                "are='" + are + '\'' +
                ", adress='" + adress + '\'' +
                ", state='" + state + '\'' +
                ", lnt='" + lnt + '\'' +
                ", lat='" + lat + '\'' +
                ", name='" + name + '\'' +
                ", end_time='" + end_time + '\'' +
                ", constructionid='" + constructionid + '\'' +
                ", start_time='" + start_time + '\'' +
                ", flag=" + flag +
                ", constructor=" + constructor +
                ", rg='" + rg + '\'' +
                ", inspector=" + inspector +
                ", pm=" + pm +
                ", supervisor=" + supervisor +
                ", expand=" + expand +
                '}';
    }

    public String getAre() {
        return are;
    }

    public void setAre(String are) {
        this.are = are;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getConstructionid() {
        return constructionid;
    }

    public void setConstructionid(String constructionid) {
        this.constructionid = constructionid;
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

    public HisInspectorDataLitepal getConstructor() {
        return constructor;
    }

    public void setConstructor(HisInspectorDataLitepal constructor) {
        this.constructor = constructor;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public InspectorDataLitepal getInspector() {
        return inspector;
    }

    public void setInspector(InspectorDataLitepal inspector) {
        this.inspector = inspector;
    }

    public HisPmDataLitepal getPm() {
        return pm;
    }

    public void setPm(HisPmDataLitepal pm) {
        this.pm = pm;
    }

    public SupervisorDataLitepal getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorDataLitepal supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
