package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.SupervisorDataLitepal;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/5.
 */
public class SignViewDataLitepal extends DataSupport {
    private String state;
    private String checkitem;
    private String ymd;
    private ShigongfangistBean construction;
    private int flag;
    private String rg;//_id更换成rg
    private InspectorDataLitepal inspector;
    private boolean expand;
    public SignViewDataLitepal() {

    }

    public SignViewDataLitepal(String state, String checkitem, String ymd, ShigongfangistBean construction, int flag, String rg, InspectorDataLitepal inspector, boolean expand) {
        this.state = state;
        this.checkitem = checkitem;
        this.ymd = ymd;
        this.construction = construction;
        this.flag = flag;
        this.rg = rg;
        this.inspector = inspector;
        this.expand = expand;
    }

    @Override
    public String toString() {
        return "SignViewDataLitepal{" +
                "state='" + state + '\'' +
                ", checkitem='" + checkitem + '\'' +
                ", ymd='" + ymd + '\'' +
                ", construction=" + construction +
                ", flag=" + flag +
                ", rg='" + rg + '\'' +
                ", inspector=" + inspector +
                ", expand=" + expand +
                '}';
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCheckitem() {
        return checkitem;
    }

    public void setCheckitem(String checkitem) {
        this.checkitem = checkitem;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public ShigongfangistBean getConstruction() {
        return construction;
    }

    public void setConstruction(ShigongfangistBean construction) {
        this.construction = construction;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
