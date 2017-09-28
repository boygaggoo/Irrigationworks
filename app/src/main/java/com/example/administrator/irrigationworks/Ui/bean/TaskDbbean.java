package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/6.
 */
public class TaskDbbean {
     private String checkitem;
     private String state;
     private ShigongfangistBean construction;
     private InspectorDataLitepal inspector;
    private boolean expand;

    public TaskDbbean() {
    }

    @Override
    public String toString() {
        return "TaskDbbean{" +
                "checkitem='" + checkitem + '\'' +
                ", state='" + state + '\'' +
                ", construction=" + construction +
                ", inspector=" + inspector +
                ", expand=" + expand +
                '}';
    }

    public TaskDbbean(String checkitem, String state, ShigongfangistBean construction, InspectorDataLitepal inspector, boolean expand) {
        this.checkitem = checkitem;
        this.state = state;
        this.construction = construction;
        this.inspector = inspector;
        this.expand = expand;
    }

    public String getCheckitem() {
        return checkitem;
    }

    public void setCheckitem(String checkitem) {
        this.checkitem = checkitem;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ShigongfangistBean getConstruction() {
        return construction;
    }

    public void setConstruction(ShigongfangistBean construction) {
        this.construction = construction;
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
