package com.example.administrator.irrigationworks.Ui.litepal;

import org.litepal.crud.DataSupport;

/**
 * 角色类
 * Created by Administrator on 2017/9/5.
 */
public class RoleBeanLitepal extends DataSupport {
    private String logintime;
    private String role;
    private Double flag;
    private String certificatetype;
    private String facepic;
    private String certificateno;
    private String userid;
    private String realname;
    private String token;
    private String institution;
    private String identification;
    private String phone;
    private String audit_status;
    private String job;
    private String rg;
    private String username;

    public RoleBeanLitepal() {

    }

    public RoleBeanLitepal(String logintime, String role, Double flag, String certificatetype, String facepic, String certificateno, String userid, String realname, String token, String institution, String identification, String phone, String audit_status, String job, String rg, String username) {
        this.logintime = logintime;
        this.role = role;
        this.flag = flag;
        this.certificatetype = certificatetype;
        this.facepic = facepic;
        this.certificateno = certificateno;
        this.userid = userid;
        this.realname = realname;
        this.token = token;
        this.institution = institution;
        this.identification = identification;
        this.phone = phone;
        this.audit_status = audit_status;
        this.job = job;
        this.rg = rg;
        this.username = username;
    }

    @Override
    public String toString() {
        return "RoleBeanLitepal{" +
                "logintime='" + logintime + '\'' +
                ", role='" + role + '\'' +
                ", flag=" + flag +
                ", certificatetype='" + certificatetype + '\'' +
                ", facepic='" + facepic + '\'' +
                ", certificateno='" + certificateno + '\'' +
                ", userid='" + userid + '\'' +
                ", realname='" + realname + '\'' +
                ", token='" + token + '\'' +
                ", institution='" + institution + '\'' +
                ", identification='" + identification + '\'' +
                ", phone='" + phone + '\'' +
                ", audit_status='" + audit_status + '\'' +
                ", job='" + job + '\'' +
                ", rg='" + rg + '\'' +
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

    public Double getFlag() {
        return flag;
    }

    public void setFlag(Double flag) {
        this.flag = flag;
    }

    public String getCertificatetype() {
        return certificatetype;
    }

    public void setCertificatetype(String certificatetype) {
        this.certificatetype = certificatetype;
    }

    public String getFacepic() {
        return facepic;
    }

    public void setFacepic(String facepic) {
        this.facepic = facepic;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
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

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
