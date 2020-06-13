package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.ProductRel;
import com.dhcc.entity.SubAccount;
import com.dhcc.util.MyCsvUtil;

import java.util.*;

public class TianShanSubAccountSEVEN {
    private static  HashMap<String, ProductRel> productRels =new HashMap<>();


    public static void main(String[] args) {
        String fileName="子账户.csv";
        //新建de_mst中转表
        String de_mst_mid ="create table de_mst_mid\n" +
                "(\n" +
                "  acc_id           NUMBER(12) not null,\n" +
                "  ag_acc_no        VARCHAR2(32),\n" +
                "  ag_acc_seqn      VARCHAR2(20),\n" +
                "  acc_seqn         NUMBER(6) not null,\n" +
                "  opn_br_no        VARCHAR2(10),\n" +
                "  ag_br_no         VARCHAR2(10),\n" +
                "  prdt_no          VARCHAR2(32),\n" +
                "  cur_no           VARCHAR2(3),\n" +
                "  de_bal           NUMBER(22,2),\n" +
                "  de_book_bal      NUMBER(22,2),\n" +
                "  de_ys_bal        NUMBER(22,2),\n" +
                "  de_rate          NUMBER(10,6),\n" +
                "  de_hst_cnt       NUMBER(6),\n" +
                "  de_opn_date      VARCHAR2(8),\n" +
                "  de_ic_date       VARCHAR2(8),\n" +
                "  de_last_date     VARCHAR2(8),\n" +
                "  de_mtr_date      VARCHAR2(8),\n" +
                "  de_acc_sts       VARCHAR2(4),\n" +
                "  de_hold_sts      VARCHAR2(4),\n" +
                "  de_hold_amt      NUMBER(22,2),\n" +
                "  de_stop_pay_sts  VARCHAR2(4),\n" +
                "  de_stop_pay_amt  NUMBER(22,2),\n" +
                "  de_ctl_amt       NUMBER(22,2),\n" +
                "  de_od_flag       VARCHAR2(4),\n" +
                "  de_cif_no        NUMBER(20),\n" +
                "  de_open_flag     VARCHAR2(10),\n" +
                "  de_mac           VARCHAR2(32),\n" +
                "  de_ints_date     VARCHAR2(8),\n" +
                "  de_cif_last_date VARCHAR2(8),\n" +
                "  de_rate_type     VARCHAR2(4),\n" +
                "  de_cash_flag     VARCHAR2(4),\n" +
                "  de_acc_type      VARCHAR2(4),\n" +
                "  de_intst         NUMBER(22,2),\n" +
                "  de_unpay_intst   NUMBER(22,2),\n" +
                "  de_amt1          NUMBER(22,2),\n" +
                "  de_amt2          NUMBER(22,2),\n" +
                "  prdt_trade_date  VARCHAR2(20),\n" +
                "  de_tmp  VARCHAR2(32)\n" +
                ");";
        List<CsvRow> list = MyCsvUtil.getData(fileName);
//        list.remove(0);
        List<SubAccount> subAccounts=toSubAccount(list);
        //事先维护产品工厂和互金产品号的对应关系
//        HashMap<String, ProductRel> productRels = getProductRels();
//        HashMap<String, EcifTemp> ecifTempsHashMap = TempDateUtil.getEcifTemps();
        Map<String,Integer> count = new HashMap<String,Integer>();
        StringBuilder stringBuilder=new StringBuilder();
        for(SubAccount subAccount: subAccounts){
            String acc_no = subAccount.get电子账号();
            if(!"6099".equals(subAccount.get旧的产品号())){
                if(count.containsKey(acc_no)){
                    int oldValue = count.get(acc_no);
                    count.put(acc_no,oldValue+1);
                }else{
                    count.put(acc_no,1);
                }
            }
        }
        int acc_id = 0;
       /* String modifyDemst = " ALTER TABLE \"DE_MST\" MODIFY (\"AG_ACC_SEQN\" VARCHAR2(20));";
        String addFiled = "alter table DE_MST add de_tmp varchar2(32);";
        stringBuilder.append(modifyDemst+"\n");
        stringBuilder.append(addFiled+"\n");*/
        stringBuilder.append(de_mst_mid+"\n");
        for (SubAccount subAccount: subAccounts) {
            String 产品名称=subAccount.get产品名称();
            //直接从行里提供的csv数据的客户号、电子账户都为旧的客户号，电子账户
            String 旧的客户号 = subAccount.get客户号();
            String ag_acc_no = subAccount.get电子账号();
            String ag_acc_seqn =subAccount.get账户序号();
            acc_id+=1;

            String opn_br_no= Constant.tx_br_no;
//            ProductRel productRel = productRels.get(old_prdt_no);
            String prdt_no = subAccount.get旧的产品号();
            String de_bal = subAccount.get账户余额();
            String de_book_bal = "0";
            String de_ys_bal = "0";
            String de_rate=subAccount.get利率();
            int de_hst_cnt=0;
            String de_opn_date=subAccount.get开户时间().trim();
            de_opn_date = de_opn_date.substring(0,10).replace("-","");
            String de_ic_date=subAccount.get起息日();
            /*if(de_ic_date!=null || !"".equals(de_ic_date)){
                System.out.println(de_ic_date);
                de_ic_date.substring(0,10).replaceAll("-","");
            }*/
            //最后动账日= 上笔发生日
            String de_last_date=subAccount.get上次结息日();
            String de_mtr_date=subAccount.get终止日();
            String de_acc_sts=subAccount.get账户状态();
            String de_hold_sts = subAccount.get冻结状态();
            String de_hold_amt=subAccount.get冻结金额();
            if(de_hold_amt.equals("\\N") || de_hold_amt.equals("0.00")){
                de_hold_amt = "0";
            }
            String de_stop_pay_sts=subAccount.get止付状态();
            String de_stop_pay_amt = subAccount.get止付金额();
            String de_ctl_amt = subAccount.get控制金额();
            String de_od_flag=subAccount.get透支标志();
            String de_cif_no=subAccount.get客户号();
            String de_open_flag="0000000000";
            String de_ints_date=subAccount.get上次结息日();
            String de_cif_last_date = subAccount.get最后动账日();
            String de_rate_type="RT00";
            String de_cash_flag="CF00";
            //结息日期= 上次结息日
            String de_acc_type="AT01";
            String de_intst= "0";//subAccount.get累计收益();

            int acc_seqn =0;
            if(!"6099".equals(subAccount.get旧的产品号())){
                if(count.get(ag_acc_no)>0){
                    acc_seqn = 20000+count.get(ag_acc_no);
                    count.put(ag_acc_no,count.get(ag_acc_no)-1);
                }
            }
            if("6099".equals(subAccount.get旧的产品号())){
                acc_seqn = 1;
            }
            String de_mst="INSERT INTO  de_mst_mid (\"ACC_ID\", \"AG_ACC_NO\", \"AG_ACC_SEQN\", \"ACC_SEQN\", \"OPN_BR_NO\", \"AG_BR_NO\", \"PRDT_NO\", \"CUR_NO\", \"DE_BAL\", \"DE_BOOK_BAL\", \"DE_YS_BAL\", \"DE_RATE\", " +
                    "\"DE_HST_CNT\", \"DE_OPN_DATE\", \"DE_IC_DATE\", \"DE_LAST_DATE\", \"DE_MTR_DATE\", \"DE_ACC_STS\", \"DE_HOLD_STS\", \"DE_HOLD_AMT\", \"DE_STOP_PAY_STS\", \"DE_STOP_PAY_AMT\", \"DE_CTL_AMT\", \"DE_OD_FLAG\", \"DE_CIF_NO\", \"DE_OPEN_FLAG\"," +
                    " \"DE_MAC\", \"DE_INTS_DATE\", \"DE_CIF_LAST_DATE\", " +
                    "\"DE_RATE_TYPE\", \"DE_CASH_FLAG\", \"DE_ACC_TYPE\", \"DE_INTST\", \"DE_UNPAY_INTST\", \"DE_AMT1\", \"DE_AMT2\",\"PRDT_TRADE_DATE\",\"DE_TMP\")" +
                    " VALUES ("+acc_id+", '"+ag_acc_no+"', '"+ag_acc_seqn+"', "+acc_seqn+", '"+opn_br_no+"', '"+opn_br_no+"', '"+prdt_no+"', '156', "+de_bal+", "+de_book_bal+", "+de_ys_bal+", "+de_rate+", " +
                    ""+de_hst_cnt+", '"+de_opn_date+"', '"+de_ic_date+"', '"+de_last_date +"', '"+de_mtr_date+"', '"+de_acc_sts+"', '"+de_hold_sts+"', "+de_hold_amt+", '"+de_stop_pay_sts+"', "+de_stop_pay_amt+", "+de_ctl_amt+", '"+de_od_flag+"', " +
                    " NULL, '"+de_open_flag+"', NULL, '"+de_ints_date+"', '"+de_cif_last_date+"', '"+de_rate_type+"', '"+de_cash_flag+"', '"+de_acc_type+"', " +
                    ""+de_intst+",NULL, NULL, NULL, NULL,'"+de_cif_no+"');\n";
            stringBuilder.append(de_mst);
        }




        String updateCifNo = "update de_mst_mid de set de_cif_no = (select cif_no_new from "+Constant.database_ecif+".cif_info_mid cim where de.de_tmp=cim.cif_no_old)" +
                " where exists （select 1 from "+Constant.database_ecif+".cif_info_mid cim where de.de_tmp=cim.cif_no_old) and opn_br_no='"+Constant.tx_br_no+"';";
        String deInstDate = "UPDATE de_mst_mid set de_ints_date =NULL  where de_ints_date ='\\N' and opn_br_no ='"+Constant.tx_br_no+"';";
        String deLastDate = "UPDATE de_mst_mid set de_last_date =NULL  where de_last_date ='\\N' and opn_br_no ='"+Constant.tx_br_no+"';";
        String deHoldAmt = "UPDATE de_mst_mid set de_hold_amt =NULL  where de_hold_amt ='\\N' and opn_br_no ='"+Constant.tx_br_no+"';";
        String deMtrDate = "UPDATE de_mst_mid set de_mtr_date ='99991231'  where de_mtr_date ='\\N' and opn_br_no ='"+Constant.tx_br_no+"';";
        /*String updateAgaccno = "  update de_mst_mid a set (ag_acc_no,acc_id)=(select b.acc_no,b.acc_id from mdm_acc_rel b where a.de_cif_no=b.cif_no and opn_br_no='"+Constant.tx_br_no+"' " +
                " and a.de_cif_no is not null) where exists （select 1 from mdm_acc_rel b where a.de_cif_no=b.cif_no and " +
                " opn_br_no='"+Constant.tx_br_no+"' and a.de_cif_no is not null);";*/
        String updateAgaccno ="update de_mst_mid a set (ag_acc_no,acc_id) = (select b.acc_no,b.acc_id from mdm_acc_rel b " +
                "where b.opn_br_no = '"+Constant.tx_br_no+"' and b.cif_no = a.de_cif_no) " +
                "where a.opn_br_no = '"+Constant.tx_br_no+"' and a.de_cif_no is not null" +
                " and a.de_cif_no in (select b.cif_no from mdm_acc_rel b where b.opn_br_no = '"+Constant.tx_br_no+"' ) ; \n";

//        String seqn_max = "select acc_seqn_max from acc_seqn where acc_id='' and acc_seqn_type = 'ST03'";
        String prdt_no = "update de_mst_mid de set prdt_no =  (select prdt_no from prdt_mapper pm where de.prdt_no=pm.prdt_no_old and opn_br_no='"+Constant.tx_br_no+"') " +
                " where exists （select 1 from prdt_mapper pm where de.prdt_no=pm.prdt_no_old and opn_br_no='"+Constant.tx_br_no+"');";

        String updateAgaccseqn = " update de_mst_mid de set de.ag_acc_seqn =  (select acc_seqn_new from ac_seqn_map asn where de.ag_acc_no=asn.acc_no_new " +
                " and de.ag_acc_seqn=asn.acc_seqn_old and opn_br_no='"+Constant.tx_br_no+"') where exists （select 1 from ac_seqn_map asn where " +
                " de.ag_acc_no=asn.acc_no_new and de.ag_acc_seqn=asn.acc_seqn_old and opn_br_no='"+Constant.tx_br_no+"');";

        //更新de_mst后 acc_id后插入acc_seqn表中记录 acc_seqn_max  因acc_id没有重复 最大值默认为 20001
        String updateAccSeqn = "insert into acc_seqn (acc_id,acc_seqn_type,acc_seqn_max)   select a.acc_id,b.acc_seqn_type,b.acc_seqn from de_mst_mid a ,(select ag_acc_no,'ST03' as acc_seqn_type,  max(acc_seqn) as acc_seqn from de_mst_mid " +
                " where opn_br_no='"+Constant.tx_br_no+"' and acc_seqn !=1 group by ag_acc_no union (select ag_acc_no,'ST00' as acc_seqn_type,  acc_seqn from de_mst_mid " +
                " where opn_br_no='"+Constant.tx_br_no+"' and acc_seqn =1)) b where a.ag_acc_no = b.ag_acc_no and  a.acc_seqn = b.acc_seqn " +
                " and not exists(select 1 from acc_seqn c where c.acc_id=a.acc_id);";

        //de_mst表数据更新完成之后执行
        String insertEcifPrdtSignInfo = "insert into ecif_sign_prdt_info select de_cif_no,cif_name,'CT01',prdt_no,NULL,100,ag_acc_no,acc_seqn,de_mtr_date,de_opn_date,NULL,'0061',NULL,ag_acc_no,1,NULL,NULL,'SS00',NULL,NULL,'BF01','"+Constant.br_no+"' from " +
                " ((select de_cif_no,ag_acc_no,acc_seqn,prdt_no,de_mtr_date,de_opn_date from de_mst_mid " +
                " where opn_br_no ='"+Constant.tx_br_no+"'  and acc_seqn != 1 and de_cif_no is not null) a left join "+Constant.database_ecif+".cif_info_mid b on a.de_cif_no = b.cif_no_new);";
        //更新完de_mst_mid 表后 筛选正确的信息 插入de_mst中
        String insertDeMst ="insert into  de_mst select acc_id, ag_acc_no, ag_acc_seqn, acc_seqn, opn_br_no, ag_br_no, prdt_no, cur_no, de_bal, de_book_bal, de_ys_bal, de_rate, " +
                "de_hst_cnt, de_opn_date, de_ic_date, de_last_date, de_mtr_date, de_acc_sts, de_hold_sts, de_hold_amt, de_stop_pay_sts, de_stop_pay_amt, de_ctl_amt, de_od_flag, " +
                "de_cif_no, de_open_flag,de_mac, de_ints_date, de_cif_last_date,de_rate_type, de_cash_flag, de_acc_type, de_intst, de_unpay_intst, de_amt1, de_amt2,prdt_trade_date from" +
                " de_mst_mid b where de_cif_no is not null and  length(acc_id)=12 and length(ag_acc_seqn)<=2 and not exists (select 1 from de_mst a where a.ag_acc_no = b.ag_acc_no and \n" +
                " a.ag_acc_seqn = b.ag_acc_seqn and a.opn_br_no = '"+Constant.tx_br_no+"');\n";
        stringBuilder.append(updateCifNo+"\n");
        stringBuilder.append(deInstDate+"\n");
        stringBuilder.append(deLastDate+"\n");
        stringBuilder.append(deMtrDate+"\n");
//        stringBuilder.append(deHoldAmt+"\n");
        stringBuilder.append(updateAgaccno+"\n");
        stringBuilder.append(prdt_no+"\n");
        stringBuilder.append(updateAgaccseqn+"\n");
        stringBuilder.append(insertDeMst+"\n");
        stringBuilder.append(updateAccSeqn+"\n");
        stringBuilder.append(insertEcifPrdtSignInfo+"\n");

        /*String modifyDemstEnd = " ALTER TABLE \"DE_MST\" MODIFY (\"AG_ACC_SEQN\" VARCHAR2(12));";
        String deleteFiled = " alter table DE_MST drop column de_tmp;";
        //手动删除de_tmp
        stringBuilder.append(modifyDemstEnd+"\n");
        stringBuilder.append(deleteFiled+"\n");*/
        MyCsvUtil.writFile(stringBuilder.toString(),"demst.cvs");

    }



    public static List<SubAccount> toSubAccount(List<CsvRow> rows) {
        List<SubAccount> subAccounts=new ArrayList<SubAccount>(rows.size());
        for (CsvRow csvRow : rows) {
            Object[] object = csvRow.toArray();
            String 客户号 = object[0].toString().trim();
            String 电子账号 = object[1].toString().trim();
            String 账户序号 = object[2].toString().trim();
            String 旧的产品号 = object[3].toString().trim();
            String 开户时间 = object[4].toString().trim();
            String 起息日 = object[5].toString().trim();
            String 终止日 = object[6].toString().trim();
            String 上次结息日 = object[7].toString().trim();
            String 产品名称 = object[8].toString().trim();
            String 账户余额 = object[9].toString().trim();
            String 冻结状态 = object[10].toString().trim();
            String 冻结金额 = object[11].toString().trim();
            String 止付状态 = object[12].toString().trim();
            String 止付金额 = object[13].toString().trim();
            String 控制金额 = object[14].toString().trim();
            String 利率 = object[15].toString().trim();
            String 积数 = object[16].toString().trim();
            String 最后动账日 = object[17].toString().trim();
            String 累计收益 = object[18].toString().trim();
            String 预计收益 = object[19].toString().trim();
            String 透支标志 = object[20].toString().trim();
            String 开户利率 = object[21].toString().trim();
            String 账户状态 = object[22].toString().trim();
            String 提现标识 = object[23].toString().trim();
            String 签约状态 = object[24].toString().trim();
            String 客户经理 = object[25].toString().trim();
            String 签约日期 = object[26].toString().trim();
            String 开户金额 = object[27].toString().trim();
            String 开户渠道 = object[28].toString().trim();

            SubAccount subAccount=new SubAccount(  客户号,  电子账号,  旧的产品号,
                     开户时间,  起息日,  终止日,  上次结息日,  产品名称,
                     冻结状态,  止付状态,  利率,  积数,  最后动账日,
                     透支标志,  开户利率,  账户状态,  提现标识,  签约状态,
                     客户经理,  签约日期,  开户金额,  开户渠道,  账户余额,
                     控制金额,  累计收益,  预计收益,
                     冻结金额,  止付金额,  账户序号);
            subAccounts.add(subAccount);
        }
        return subAccounts;
    }

    public static  HashMap<String, ProductRel> getProductRels() {
        if (productRels.size()>0){
            return productRels;
        }
        String fileName = "天山新旧产品对照表.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        return  toProductRels(rows);
    }

    public static HashMap<String, ProductRel> toProductRels(List<CsvRow> rows) {
        for (CsvRow row : rows) {
            Object[] object = row.toArray();
            String prdt_name = object[0].toString().trim();
            String old_prdt_no = object[1].toString().trim();
            String prdt_no = object[2].toString().trim();
            String factory_prdt_no = object[3].toString().trim();
            ProductRel productRel=new ProductRel(old_prdt_no, prdt_no, prdt_name, factory_prdt_no);
            productRels.put(old_prdt_no,productRel);
        }
        return productRels;
    }
}


/*
* 最新添加客户号信息重新生成后：
	//先更新cif_info_mid
	①：update icore_ecif_bth.cif_info_mid cim set cif_no_new = (select cif_no from icore_ecif_bth.ecif_cert_info eci where br_no='850000' and eci.cert_no=cim.cert_no)
	//更新mdm_acc_rel
	②：update mdm_acc_rel mar set cif_no = (select cif_no from icore_ecif_bth.ecif_cert_info eci where br_no='850000' and eci.cert_no=mar.cert_no) where opn_br_no='850088'
	//更新de_mst_mid
	更新客户号
	③:update de_mst_mid dmm set de_cif_no = (select cif_no_new from icore_ecif_bth.cif_info_mid cim where cim.cif_no_old=dmm.de_tmp) where opn_br_no='850088' and de_cif_no is null
	 根据客户号更新电子账号和acc_id
	 update de_mst_mid a set (ag_acc_no,acc_id) = (select b.acc_no,b.acc_id from mdm_acc_rel b where b.opn_br_no = '850088' and b.cif_no = a.de_cif_no) where a.opn_br_no = '850088' and a.de_cif_no is not null and a.de_cif_no in (select b.cif_no from mdm_acc_rel b where b.opn_br_no = '850088' ) ;
	 根据账号更新ag_acc_seqn
	 update de_mst_mid de set de.ag_acc_seqn =  (select acc_seqn_new from ac_seqn_map asn where de.ag_acc_no=asn.acc_no_new and de.ag_acc_seqn=asn.acc_seqn_old and opn_br_no='850088') where length(ag_acc_seqn)>2
	④：将更新完成后正确的数据插入de_mst
	insert into  de_mst select acc_id, ag_acc_no, ag_acc_seqn, acc_seqn, opn_br_no, ag_br_no, prdt_no, cur_no, de_bal, de_book_bal, de_ys_bal, de_rate,
                de_hst_cnt, de_opn_date, de_ic_date, de_last_date, de_mtr_date, de_acc_sts, de_hold_sts, de_hold_amt, de_stop_pay_sts, de_stop_pay_amt, de_ctl_amt, de_od_flag,
                de_cif_no, de_open_flag,de_mac, de_ints_date, de_cif_last_date,de_rate_type, de_cash_flag, de_acc_type, de_intst, de_unpay_intst, de_amt1, de_amt2,prdt_trade_date from
                 de_mst_mid b where de_cif_no is not null and  length(acc_id)=12 and length(ag_acc_seqn)<=2
                 and not exists (select 1 from de_mst a where a.ag_acc_no = b.ag_acc_no and a.ag_acc_seqn = b.ag_acc_seqn and a.opn_br_no = '850088')


	⑤：insert into acc_seqn (acc_id,acc_seqn_type,acc_seqn_max)   select a.acc_id,b.acc_seqn_type,b.acc_seqn from de_mst_mid a ,(select ag_acc_no,'ST03' as acc_seqn_type,  max(acc_seqn) as acc_seqn from de_mst_mid
                where opn_br_no='850088' and acc_seqn !=1 group by ag_acc_no union (select ag_acc_no,'ST00' as acc_seqn_type,  acc_seqn from de_mst_mid
                where opn_br_no='850088' and acc_seqn =1)) b where a.ag_acc_no = b.ag_acc_no and  a.acc_seqn = b.acc_seqn
                and not exists(select 1 from acc_seqn c where c.acc_id=a.acc_id);


*
*   重新导入de_mst表数据时，准备：
    delete from acc_seqn where acc_id in(select acc_id from de_mst_mid)
    select * from ecif_sign_prdt_info where br_no='850000'
    delete from ecif_sign_prdt_info where br_no='850000'
    select * from de_mst where opn_br_no='850088'
    delete from de_mst where opn_br_no='850088',
    删除de_mst_mid表
* */