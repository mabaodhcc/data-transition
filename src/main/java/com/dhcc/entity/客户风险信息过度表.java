package com.dhcc.entity;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-25 10:05
 * @version: 1.0.0
 */
public class 客户风险信息过度表 {
    private String cif_no_old;
    private String date;
    private String risk_rvl;

    public String getCif_no_old() {
        return cif_no_old;
    }

    public void setCif_no_old(String cif_no_old) {
        this.cif_no_old = cif_no_old;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRisk_rvl() {
        return risk_rvl;
    }

    public void setRisk_rvl(String risk_rvl) {
        this.risk_rvl = risk_rvl;
    }
}
