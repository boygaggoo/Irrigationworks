package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by Administrator on 2017/9/20.
 */
public class AuditStatusBean {
    private String audit_status;

    public AuditStatusBean() {
    }

    public AuditStatusBean(String audit_status) {
        this.audit_status = audit_status;
    }

    @Override
    public String toString() {
        return "AuditStatusBean{" +
                "audit_status='" + audit_status + '\'' +
                '}';
    }

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }
}
