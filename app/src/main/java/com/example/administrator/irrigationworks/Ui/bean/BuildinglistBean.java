package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by Administrator on 2017/8/15.
 */
public class BuildinglistBean {
    private String sutupe;
    private String NAME;
    private String lag;
    private String Lng;
    private boolean expand;
    public BuildinglistBean() {
        this.sutupe = sutupe;
    }

    public BuildinglistBean(String sutupe, String NAME, String lag, String lng, boolean expand) {
        this.sutupe = sutupe;
        this.NAME = NAME;
        this.lag = lag;
        Lng = lng;
        this.expand = expand;
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

    public String getLag() {
        return lag;
    }

    public void setLag(String lag) {
        this.lag = lag;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    @Override
    public String toString() {
        return "BuildinglistBean{" +
                "sutupe='" + sutupe + '\'' +
                ", NAME='" + NAME + '\'' +
                ", lag='" + lag + '\'' +
                ", Lng='" + Lng + '\'' +
                ", expand=" + expand +
                '}';
    }
}
