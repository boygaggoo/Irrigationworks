package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by Administrator on 2017/8/15.
 */
public class InfoBean {
    private String code;
    private String message;
    private String id;
    private String name;
    private String tele;

    public InfoBean() {
    }

    public InfoBean(String code, String message, String id, String name, String tele) {
        this.code = code;
        this.message = message;
        this.id = id;
        this.name = name;
        this.tele = tele;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    @Override
    public String toString() {
        return "InfoBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tele='" + tele + '\'' +
                '}';
    }
}
