package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by Administrator on 2017/8/15.
 */
public class BuildingSiteBean {
    private String sutupe;
    private String NAME;

    public BuildingSiteBean() {
        this.sutupe = sutupe;
    }

    public BuildingSiteBean(String sutupe, String NAME) {
        this.sutupe = sutupe;
        this.NAME = NAME;
    }

    public String getSutupe() {
        return sutupe;
    }

    public void setSutupe(String sutupe) {
        this.sutupe = sutupe;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return "BuildingSiteBean{" +
                "sutupe='" + sutupe + '\'' +
                ", NAME='" + NAME + '\'' +
                '}';
    }
}
