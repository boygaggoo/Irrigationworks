package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;

/**
 * Created by Administrator on 2017/9/7.
 */
public class GeDangerlistBean {
    private String constructionid;
    private String createtime;
    private int constructor_readed;//施工方已读
    private int supervisor_readed;//总监理已读
    private int chiefsupervisor_confirm;////总监理确认
    private int pm_confirm;//项目经理 1已经整改了 0没有整改
    private int flag;
    private int timeout;
    private String remark;//上报内容
    private String report_content;//上报标题
    private String checkitem;//检查项目
    private String report_time;//上报时间
    private String date_time;//上报时间
    private ZhengGaiBean rectificationInfo;//上报时间
    private InspectorDataLitepal inspector;//巡查员
    private String rg;
    private String state;
    private String taskid;
    private boolean expand;
    private TotalGeDangerlistBean construction;//所有人员
    public GeDangerlistBean() {

    }

    public GeDangerlistBean(String constructionid, String createtime, int constructor_readed, int supervisor_readed, int chiefsupervisor_confirm, int pm_confirm, int flag, int timeout, String remark, String report_content, String checkitem, String report_time, String date_time, ZhengGaiBean rectificationInfo, InspectorDataLitepal inspector, String rg, String state, String taskid, boolean expand, TotalGeDangerlistBean construction) {
        this.constructionid = constructionid;
        this.createtime = createtime;
        this.constructor_readed = constructor_readed;
        this.supervisor_readed = supervisor_readed;
        this.chiefsupervisor_confirm = chiefsupervisor_confirm;
        this.pm_confirm = pm_confirm;
        this.flag = flag;
        this.timeout = timeout;
        this.remark = remark;
        this.report_content = report_content;
        this.checkitem = checkitem;
        this.report_time = report_time;
        this.date_time = date_time;
        this.rectificationInfo = rectificationInfo;
        this.inspector = inspector;
        this.rg = rg;
        this.state = state;
        this.taskid = taskid;
        this.expand = expand;
        this.construction = construction;
    }

    @Override
    public String toString() {
        return "GeDangerlistBean{" +
                "constructionid='" + constructionid + '\'' +
                ", createtime='" + createtime + '\'' +
                ", constructor_readed=" + constructor_readed +
                ", supervisor_readed=" + supervisor_readed +
                ", chiefsupervisor_confirm=" + chiefsupervisor_confirm +
                ", pm_confirm=" + pm_confirm +
                ", flag=" + flag +
                ", timeout=" + timeout +
                ", remark='" + remark + '\'' +
                ", report_content='" + report_content + '\'' +
                ", checkitem='" + checkitem + '\'' +
                ", report_time='" + report_time + '\'' +
                ", date_time='" + date_time + '\'' +
                ", rectificationInfo=" + rectificationInfo +
                ", inspector=" + inspector +
                ", rg='" + rg + '\'' +
                ", state='" + state + '\'' +
                ", taskid='" + taskid + '\'' +
                ", expand=" + expand +
                ", construction=" + construction +
                '}';
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

    public int getConstructor_readed() {
        return constructor_readed;
    }

    public void setConstructor_readed(int constructor_readed) {
        this.constructor_readed = constructor_readed;
    }

    public int getSupervisor_readed() {
        return supervisor_readed;
    }

    public void setSupervisor_readed(int supervisor_readed) {
        this.supervisor_readed = supervisor_readed;
    }

    public int getChiefsupervisor_confirm() {
        return chiefsupervisor_confirm;
    }

    public void setChiefsupervisor_confirm(int chiefsupervisor_confirm) {
        this.chiefsupervisor_confirm = chiefsupervisor_confirm;
    }

    public int getPm_confirm() {
        return pm_confirm;
    }

    public void setPm_confirm(int pm_confirm) {
        this.pm_confirm = pm_confirm;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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

    public String getCheckitem() {
        return checkitem;
    }

    public void setCheckitem(String checkitem) {
        this.checkitem = checkitem;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public ZhengGaiBean getRectificationInfo() {
        return rectificationInfo;
    }

    public void setRectificationInfo(ZhengGaiBean rectificationInfo) {
        this.rectificationInfo = rectificationInfo;
    }

    public InspectorDataLitepal getInspector() {
        return inspector;
    }

    public void setInspector(InspectorDataLitepal inspector) {
        this.inspector = inspector;
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

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public TotalGeDangerlistBean getConstruction() {
        return construction;
    }

    public void setConstruction(TotalGeDangerlistBean construction) {
        this.construction = construction;
    }
}
