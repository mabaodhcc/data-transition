package com.dhcc.entity;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-20 20:25
 * @version: 1.0.0
 */
public class AccBindBan {
    private String acc_no;
    private String bnd_acc_no;
    private String bnd_phone_no;
    private String bnd_cert_no;

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String acc_no) {
        this.acc_no = acc_no;
    }

    public String getBnd_acc_no() {
        return bnd_acc_no;
    }

    public void setBnd_acc_no(String bnd_acc_no) {
        this.bnd_acc_no = bnd_acc_no;
    }

    public String getBnd_phone_no() {
        return bnd_phone_no;
    }

    public void setBnd_phone_no(String bnd_phone_no) {
        this.bnd_phone_no = bnd_phone_no;
    }

    public String getBnd_cert_no() {
        return bnd_cert_no;
    }

    public void setBnd_cert_no(String bnd_cert_no) {
        this.bnd_cert_no = bnd_cert_no;
    }
}
