package com.dhcc.entity;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-21 11:05
 * @version: 1.0.0
 */
public class EcifMangrInfo {
    private String br_no;//机构编码
    private String mangr_no;//客户经理号
    private String mangr_name;//客户经理名称
    private String mangr_type;//客户经理类型	MT00 揽存经理 MT01 服务经理 MT02 放贷经理 MT03 营销经理
    private String mangr_seqn;//客户经理管理序号	外部客户经理的工号等
    private String mangr_sex;//客户经理性别	MS00 男MS01 女
    private String mangr_phone;//联系电话
    private String mangr_cert_type;//证件类型
    private String mangr_cert_no;//证件号码
    private String mangr_email;//电子邮箱

    private String cif_no;//客户号
    private String cif_name;//客户名称
    private String acc_no;//关系账号
    private String ban_acc_no;//卡号
    private String acc_id;//账号ID
    private String acc_seqn;//账号序号
    private String rel_dist_mode;//分配方式	DM00 比例DM01 金额
    private String rel_prdt_no;//产品号
    private String rel_ratio;//比例
    private String rel_amt;//金额
    private String rel_beg_date;//建立日期
    private String rel_end_date;//解除日期
    private String rel_sts;//状态

    @Override
    public String toString() {
        return "EcifMangrInfo{" +
                "br_no='" + br_no + '\'' +
                ", mangr_no='" + mangr_no + '\'' +
                ", mangr_name='" + mangr_name + '\'' +
                ", mangr_type='" + mangr_type + '\'' +
                ", mangr_seqn='" + mangr_seqn + '\'' +
                ", mangr_sex='" + mangr_sex + '\'' +
                ", mangr_phone='" + mangr_phone + '\'' +
                ", mangr_cert_type='" + mangr_cert_type + '\'' +
                ", mangr_cert_no='" + mangr_cert_no + '\'' +
                ", mangr_email='" + mangr_email + '\'' +
                ", cif_no='" + cif_no + '\'' +
                ", cif_name='" + cif_name + '\'' +
                ", acc_no='" + acc_no + '\'' +
                ", ban_acc_no='" + ban_acc_no + '\'' +
                ", acc_id='" + acc_id + '\'' +
                ", acc_seqn='" + acc_seqn + '\'' +
                ", rel_dist_mode='" + rel_dist_mode + '\'' +
                ", rel_prdt_no='" + rel_prdt_no + '\'' +
                ", rel_ratio='" + rel_ratio + '\'' +
                ", rel_amt='" + rel_amt + '\'' +
                ", rel_beg_date='" + rel_beg_date + '\'' +
                ", rel_end_date='" + rel_end_date + '\'' +
                ", rel_sts='" + rel_sts + '\'' +
                '}';
    }

    public String getBr_no() {
        return br_no;
    }

    public void setBr_no(String br_no) {
        this.br_no = br_no;
    }

    public String getMangr_no() {
        return mangr_no;
    }

    public String getBan_acc_no() {
        return ban_acc_no;
    }

    public void setBan_acc_no(String ban_acc_no) {
        this.ban_acc_no = ban_acc_no;
    }

    public void setMangr_no(String mangr_no) {
        this.mangr_no = mangr_no;
    }

    public String getMangr_name() {
        return mangr_name;
    }

    public void setMangr_name(String mangr_name) {
        this.mangr_name = mangr_name;
    }

    public String getMangr_type() {
        return mangr_type;
    }

    public void setMangr_type(String mangr_type) {
        this.mangr_type = mangr_type;
    }

    public String getMangr_seqn() {
        return mangr_seqn;
    }

    public void setMangr_seqn(String mangr_seqn) {
        this.mangr_seqn = mangr_seqn;
    }

    public String getMangr_sex() {
        return mangr_sex;
    }

    public void setMangr_sex(String mangr_sex) {
        this.mangr_sex = mangr_sex;
    }

    public String getMangr_phone() {
        return mangr_phone;
    }

    public void setMangr_phone(String mangr_phone) {
        this.mangr_phone = mangr_phone;
    }

    public String getMangr_cert_type() {
        return mangr_cert_type;
    }

    public void setMangr_cert_type(String mangr_cert_type) {
        this.mangr_cert_type = mangr_cert_type;
    }

    public String getMangr_cert_no() {
        return mangr_cert_no;
    }

    public void setMangr_cert_no(String mangr_cert_no) {
        this.mangr_cert_no = mangr_cert_no;
    }

    public String getMangr_email() {
        return mangr_email;
    }

    public void setMangr_email(String mangr_email) {
        this.mangr_email = mangr_email;
    }

    public String getCif_no() {
        return cif_no;
    }

    public void setCif_no(String cif_no) {
        this.cif_no = cif_no;
    }

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String acc_no) {
        this.acc_no = acc_no;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
    }

    public String getAcc_seqn() {
        return acc_seqn;
    }

    public void setAcc_seqn(String acc_seqn) {
        this.acc_seqn = acc_seqn;
    }

    public String getRel_dist_mode() {
        return rel_dist_mode;
    }

    public void setRel_dist_mode(String rel_dist_mode) {
        this.rel_dist_mode = rel_dist_mode;
    }

    public String getRel_prdt_no() {
        return rel_prdt_no;
    }

    public void setRel_prdt_no(String rel_prdt_no) {
        this.rel_prdt_no = rel_prdt_no;
    }

    public String getRel_ratio() {
        return rel_ratio;
    }

    public void setRel_ratio(String rel_ratio) {
        this.rel_ratio = rel_ratio;
    }

    public String getRel_amt() {
        return rel_amt;
    }

    public void setRel_amt(String rel_amt) {
        this.rel_amt = rel_amt;
    }

    public String getRel_beg_date() {
        return rel_beg_date;
    }

    public void setRel_beg_date(String rel_beg_date) {
        this.rel_beg_date = rel_beg_date;
    }

    public String getRel_end_date() {
        return rel_end_date;
    }

    public void setRel_end_date(String rel_end_date) {
        this.rel_end_date = rel_end_date;
    }

    public String getRel_sts() {
        return rel_sts;
    }

    public void setRel_sts(String rel_sts) {
        this.rel_sts = rel_sts;
    }

    public String getCif_name() {
        return cif_name;
    }

    public void setCif_name(String cif_name) {
        this.cif_name = cif_name;
    }
}
