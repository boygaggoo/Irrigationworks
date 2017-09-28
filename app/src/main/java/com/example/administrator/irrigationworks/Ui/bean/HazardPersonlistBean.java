package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;

/**
 * Created by Administrator on 2017/9/7.
 */
public class HazardPersonlistBean {
    private String constructionid;
    private String createtime;
    private int flag;
    private String remark;//上报内容
    private String report_content;//上报标题
    private String report_time;//上报时间
    private InspectorDataLitepal reportor;//上报人
    private String rg;
    private String state;
    private boolean expand;
    private InspectorDataLitepal constructor;//施工方
    public HazardPersonlistBean() {

    }

    public HazardPersonlistBean(String constructionid, String createtime, int flag, String remark, String report_content, String report_time, InspectorDataLitepal reportor, String rg, String state, boolean expand, InspectorDataLitepal constructor) {
        this.constructionid = constructionid;
        this.createtime = createtime;
        this.flag = flag;
        this.remark = remark;
        this.report_content = report_content;
        this.report_time = report_time;
        this.reportor = reportor;
        this.rg = rg;
        this.state = state;
        this.expand = expand;
        this.constructor = constructor;
    }

    public String getConstructionid() {
        return constructionid;
    }

    public void setConstructionid(String constructionid) {
        this.constructionid = constructionid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReport_content() {
        return report_content;
    }

    public void setReport_content(String report_content) {
        this.report_content = report_content;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public InspectorDataLitepal getReportor() {
        return reportor;
    }

    public void setReportor(InspectorDataLitepal reportor) {
        this.reportor = reportor;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public InspectorDataLitepal getConstructor() {
        return constructor;
    }

    public void setConstructor(InspectorDataLitepal constructor) {
        this.constructor = constructor;
    }

    @Override
    public String toString() {
        return "HazardPersonlistBean{" +
                "constructionid='" + constructionid + '\'' +
                ", createtime='" + createtime + '\'' +
                ", flag=" + flag +
                ", remark='" + remark + '\'' +
                ", report_content='" + report_content + '\'' +
                ", report_time='" + report_time + '\'' +
                ", reportor=" + reportor +
                ", rg='" + rg + '\'' +
                ", state='" + state + '\'' +
                ", expand=" + expand +
                ", constructor=" + constructor +
                '}';
    }
}
