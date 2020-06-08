package com.dhcc.entity;


//产品对照关系
public class PrdtRelMapper {
    private String old_prdt_no;
    private String prdt_name;

    public String getOld_prdt_no() {
        return old_prdt_no;
    }

    public void setOld_prdt_no(String old_prdt_no) {
        this.old_prdt_no = old_prdt_no;
    }

    public String getPrdt_name() {
        return prdt_name;
    }

    public void setPrdt_name(String prdt_name) {
        this.prdt_name = prdt_name;
    }

    public PrdtRelMapper(String old_prdt_no, String prdt_name) {
        this.old_prdt_no = old_prdt_no;
        this.prdt_name = prdt_name;
    }

    public PrdtRelMapper() {
    }
}
