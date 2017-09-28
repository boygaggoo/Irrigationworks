package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;

/**
 * 通知公告
 * Created by Administrator on 2017/9/15.
 */
public class NotifiBean {
    private String notice_title;
    private String users_readed;
    private InspectorDataLitepal creator;
    private String update_time;
    private String notice_content;
    private String create_time;
    private String notice_category;
    private String rg;
    private String state;
    private String noticeid;

    public NotifiBean() {
    }

    public NotifiBean(String notice_title, String users_readed, InspectorDataLitepal creator, String update_time, String notice_content, String create_time, String notice_category, String rg, String state, String noticeid) {
        this.notice_title = notice_title;
        this.users_readed = users_readed;
        this.creator = creator;
        this.update_time = update_time;
        this.notice_content = notice_content;
        this.create_time = create_time;
        this.notice_category = notice_category;
        this.rg = rg;
        this.state = state;
        this.noticeid = noticeid;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getUsers_readed() {
        return users_readed;
    }

    public void setUsers_readed(String users_readed) {
        this.users_readed = users_readed;
    }

    public InspectorDataLitepal getCreator() {
        return creator;
    }

    public void setCreator(InspectorDataLitepal creator) {
        this.creator = creator;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getNotice_category() {
        return notice_category;
    }

    public void setNotice_category(String notice_category) {
        this.notice_category = notice_category;
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

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    @Override
    public String toString() {
        return "NotifiBean{" +
                "notice_title='" + notice_title + '\'' +
                ", users_readed='" + users_readed + '\'' +
                ", creator=" + creator +
                ", update_time='" + update_time + '\'' +
                ", notice_content='" + notice_content + '\'' +
                ", create_time='" + create_time + '\'' +
                ", notice_category='" + notice_category + '\'' +
                ", rg='" + rg + '\'' +
                ", state='" + state + '\'' +
                ", noticeid='" + noticeid + '\'' +
                '}';
    }
}
