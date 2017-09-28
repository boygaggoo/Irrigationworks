package com.example.administrator.irrigationworks.Ui.bean;

/**
 * 隐患警告
 * Created by Administrator on 2017/8/15.
 */
public class WarininglistBean {
    private String sutupe;
    private String NAME;
    private String lag;
    private String Lng;
    private String warining;
    private String auditing;
    private String time;
    private boolean expand;
    public WarininglistBean() {
        this.sutupe = sutupe;
    }

    public WarininglistBean(String sutupe, String NAME, String lag, String lng, String warining, String auditing, String time, boolean expand) {
        this.sutupe = sutupe;
        this.NAME = NAME;
        this.lag = lag;
        Lng = lng;
        this.warining = warining;
        this.auditing = auditing;
        this.time = time;
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

    public String getWarining() {
        return warining;
    }

    public void setWarining(String warining) {
        this.warining = warining;
    }

    public String getAuditing() {
        return auditing;
    }

    public void setAuditing(String auditing) {
        this.auditing = auditing;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    @Override
    public String toString() {
        return "WarininglistBean{" +
                "sutupe='" + sutupe + '\'' +
                ", NAME='" + NAME + '\'' +
                ", lag='" + lag + '\'' +
                ", Lng='" + Lng + '\'' +
                ", warining='" + warining + '\'' +
                ", auditing='" + auditing + '\'' +
                ", time='" + time + '\'' +
                ", expand=" + expand +
                '}';
    }
}
