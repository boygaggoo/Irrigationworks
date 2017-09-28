package com.example.administrator.irrigationworks.Ui.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/5.
 */
public class SupervisorDataLitepal extends DataSupport {
    private String logintime;
    private String role;
    private int flag;
    private String certificatetype;
    private String certificateno;
    private String userid;
    private String realname;
    private String institution;
    private String password;
    private String identification;
    private String phone;
    private String rg;//_id更换成rg
    private String job;//_id更换成rg
    private String username;//

    public SupervisorDataLitepal() {

    }

    public SupervisorDataLitepal(String logintime, String role, int flag, String certificatetype, String certificateno, String userid, String realname, String institution, String password, String identification, String phone, String rg, String job, String username) {
        this.logintime = logintime;
        this.role = role;
        this.flag = flag;
        this.certificatetype = certificatetype;
        this.certificateno = certificateno;
        this.userid = userid;
        this.realname = realname;
        this.institution = institution;
        this.password = password;
        this.identification = identification;
        this.phone = phone;
        this.rg = rg;
        this.job = job;
        this.username = username;
    }

    @Override
    public String toString() {
        return "SupervisorDataLitepal{" +
                "logintime='" + logintime + '\'' +
                ", role='" + role + '\'' +
                ", flag=" + flag +
                ", certificatetype='" + certificatetype + '\'' +
                ", certificateno='" + certificateno + '\'' +
                ", userid='" + userid + '\'' +
                ", realname='" + realname + '\'' +
                ", institution='" + institution + '\'' +
                ", password='" + password + '\'' +
                ", identification='" + identification + '\'' +
                ", phone='" + phone + '\'' +
                ", rg='" + rg + '\'' +
                ", job='" + job + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCertificatetype() {
        return certificatetype;
    }

    public void setCertificatetype(String certificatetype) {
        this.certificatetype = certificatetype;
    }

    public String getCertificateno() {
        return certificateno;
    }

    public void setCertificateno(String certificateno) {
        this.certificateno = certificateno;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
