package com.example.administrator.irrigationworks.Ui.litepal;

import org.litepal.crud.DataSupport;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/5.
 */
public class TotalDataLitepal extends DataSupport {
    private String start_time;
    private int flag;
    private Double lnt;
    private Double lat;
    private String name;
    private String end_time;
    private String[][] coordinate;
//    private ConstructorDataLitepal constructor;
    private String rg;//_id更换成rg
    private InspectorDataLitepal inspector;
    private String state;
    private String area;
    private String constructionid;
//    private SupervisorDataLitepal supervisor;
private boolean expand;
    public TotalDataLitepal() {

    }

    public TotalDataLitepal(String start_time, int flag, Double lnt, Double lat, String name, String end_time, String[][] coordinate, String rg, InspectorDataLitepal inspector, String state, String area, String constructionid, boolean expand) {
        this.start_time = start_time;
        this.flag = flag;
        this.lnt = lnt;
        this.lat = lat;
        this.name = name;
        this.end_time = end_time;
        this.coordinate = coordinate;
        this.rg = rg;
        this.inspector = inspector;
        this.state = state;
        this.area = area;
        this.constructionid = constructionid;
        this.expand = expand;
    }

    @Override
    public String toString() {
        return "TotalDataLitepal{" +
                "start_time='" + start_time + '\'' +
                ", flag=" + flag +
                ", lnt=" + lnt +
                ", lat=" + lat +
                ", name='" + name + '\'' +
                ", end_time='" + end_time + '\'' +
                ", coordinate=" + Arrays.toString(coordinate) +
                ", rg='" + rg + '\'' +
                ", inspector=" + inspector +
                ", state='" + state + '\'' +
                ", area='" + area + '\'' +
                ", constructionid='" + constructionid + '\'' +
                ", expand=" + expand +
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

    public String[][] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String[][] coordinate) {
        this.coordinate = coordinate;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getConstructionid() {
        return constructionid;
    }

    public void setConstructionid(String constructionid) {
        this.constructionid = constructionid;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
