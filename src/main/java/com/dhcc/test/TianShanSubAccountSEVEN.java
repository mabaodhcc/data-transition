package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.entity.EcifTemp;
import com.dhcc.entity.ProductRel;
import com.dhcc.entity.SubAccount;
import com.dhcc.util.MyCsvUtil;
import com.dhcc.util.TempDateUtil;

import java.util.*;

public class TianShanSubAccountSEVEN {
    private static  HashMap<String, ProductRel> productRels =new HashMap<>();


    public static void main(String[] args) {
        String fileName="子账户.csv";
        List<CsvRow> list = MyCsvUtil.getData(fileName);
        list.remove(0);
        List<SubAccount> subAccounts=toSubAccount(list);
        //事先维护产品工厂和互金产品号的对应关系
//        HashMap<String, ProductRel> productRels = getProductRels();
//        HashMap<String, EcifTemp> ecifTempsHashMap = TempDateUtil.getEcifTemps();
        Map<String,Integer> count = new HashMap<String,Integer>();
        StringBuilder stringBuilder=new StringBuilder();
        for(SubAccount subAccount: subAccounts){
            String acc_no = subAccount.get电子账号();
            if(!subAccount.get电子账号().equals(subAccount.get账户序号())){
                if(count.containsKey(acc_no)){
                    int oldValue = count.get(acc_no);
                    count.put(acc_no,oldValue+1);
                }else{
                    count.put(acc_no,1);
                }
            }
        }
        int acc_id = 0;
        String modifyDemst = " ALTER TABLE \"DE_MST\" MODIFY (\"AG_ACC_SEQN\" VARCHAR2(20));";
        stringBuilder.append(modifyDemst+"\n");
        for (SubAccount subAccount: subAccounts) {
            String 产品名称=subAccount.get产品名称();
            //直接从行里提供的csv数据的客户号、电子账户都为旧的客户号，电子账户
            String 旧的客户号 = subAccount.get客户号();
            String ag_acc_no = subAccount.get电子账号();
            String ag_acc_seqn =subAccount.get账户序号();
            acc_id+=1;

            String opn_br_no= "770088";
            String ag_br_no= "770088";
            String old_prdt_no = subAccount.get旧的产品号();//根据旧的产品号获取互金的产品号
//            ProductRel productRel = productRels.get(old_prdt_no);
            String prdt_no = subAccount.get旧的产品号();
            String cur_no = "156";
            String de_bal = subAccount.get账户余额();
            String de_book_bal = "0";
            String de_ys_bal = "0";
            String de_rate=subAccount.get利率();
            int de_hst_cnt=0;
            String de_opn_date=subAccount.get签约日期();
            String de_ic_date=subAccount.get起息日();
            /*if(de_ic_date!=null || !"".equals(de_ic_date)){
                System.out.println(de_ic_date);
                de_ic_date.substring(0,10).replaceAll("-","");
            }*/
            //最后动账日= 上笔发生日
            String de_last_date=subAccount.get最后动账日();
            String de_mtr_date="99991231";
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
            if(!(subAccount.get电子账号().equals(subAccount.get账户序号()))){
                if(count.get(ag_acc_no)>0){
                    acc_seqn = 20000+count.get(ag_acc_no);
                    count.put(ag_acc_no,count.get(ag_acc_no)-1);
                }
            }
            if(subAccount.get电子账号().equals(subAccount.get账户序号())){
                acc_seqn = 1;
            }
            String de_mst="INSERT INTO  DE_MST (\"ACC_ID\", \"AG_ACC_NO\", \"AG_ACC_SEQN\", \"ACC_SEQN\", \"OPN_BR_NO\", \"AG_BR_NO\", \"PRDT_NO\", \"CUR_NO\", \"DE_BAL\", \"DE_BOOK_BAL\", \"DE_YS_BAL\", \"DE_RATE\", " +
                    "\"DE_HST_CNT\", \"DE_OPN_DATE\", \"DE_IC_DATE\", \"DE_LAST_DATE\", \"DE_MTR_DATE\", \"DE_ACC_STS\", \"DE_HOLD_STS\", \"DE_HOLD_AMT\", \"DE_STOP_PAY_STS\", \"DE_STOP_PAY_AMT\", \"DE_CTL_AMT\", \"DE_OD_FLAG\", \"DE_CIF_NO\", \"DE_OPEN_FLAG\"," +
                    " \"DE_MAC\", \"DE_INTS_DATE\", \"DE_CIF_LAST_DATE\", " +
                    "\"DE_RATE_TYPE\", \"DE_CASH_FLAG\", \"DE_ACC_TYPE\", \"DE_INTST\", \"DE_UNPAY_INTST\", \"DE_AMT1\", \"DE_AMT2\",\"PRDT_TRADE_DATE\")" +
                    " VALUES ("+acc_id+", '"+ag_acc_no+"', '"+ag_acc_seqn+"', "+acc_seqn+", '"+opn_br_no+"', '"+opn_br_no+"', '"+prdt_no+"', '156', "+de_bal+", "+de_book_bal+", "+de_ys_bal+", "+de_rate+", " +
                    ""+de_hst_cnt+", '"+de_opn_date+"', '"+de_ic_date+"', '"+de_last_date +"', '"+de_mtr_date+"', '"+de_acc_sts+"', '"+de_hold_sts+"', "+de_hold_amt+", '"+de_stop_pay_sts+"', "+de_stop_pay_amt+", "+de_ctl_amt+", '"+de_od_flag+"', " +
                    "(select distinct CIF_NO_NEW from  icore_ecif_bth.CIF_INFO_MID where CIF_NO_OLD = '"+de_cif_no+"'), '"+de_open_flag+"', NULL, '"+de_ints_date+"', '"+de_cif_last_date+"', '"+de_rate_type+"', '"+de_cash_flag+"', '"+de_acc_type+"', " +
                    ""+de_intst+",NULL, NULL, NULL, NULL);\n";
            stringBuilder.append(de_mst);
        }
        /*String updateCifNo = "update de_mst de set de_cif_no = (select cif_no_new from icore_ecifdb.cif_info_mid cim where de.de_cif_no=cim.cif_no_old)" +
                " where exists （select 1 from icore_ecifdb.cif_info_mid cim where de.de_cif_no=cim.cif_no_old) and opn_br_no='"+770088+"'";
*/
        String deInstDate = "UPDATE DE_mst set de_ints_date =NULL  where de_ints_date ='\\N' and opn_br_no ='770088';";
        String deHoldAmt = "UPDATE DE_mst set de_hold_amt =NULL  where de_hold_amt ='\\N' and opn_br_no ='770088';";
        /*String updateAgaccno = "  update de_mst a set (ag_acc_no,acc_id)=(select b.acc_no,b.acc_id from mdm_acc_rel b where a.de_cif_no=b.cif_no and opn_br_no='770088' " +
                " and a.de_cif_no is not null) where exists （select 1 from mdm_acc_rel b where a.de_cif_no=b.cif_no and " +
                " opn_br_no='770088' and a.de_cif_no is not null);";*/
        String updateAgaccno ="update de_mst a set (ag_acc_no,acc_id) = (select b.acc_no,b.acc_id from mdm_acc_rel b " +
                "where b.opn_br_no = '770088' and b.cif_no = a.de_cif_no) " +
                "where a.opn_br_no = '770088' and a.de_cif_no is not null; \n";

//        String seqn_max = "select acc_seqn_max from acc_seqn where acc_id='' and acc_seqn_type = 'ST03'";
        String prdt_no = "update de_mst de set prdt_no =  (select prdt_no from prdt_mapper pm where de.prdt_no=pm.prdt_no_old and opn_br_no='770088') " +
                " where exists （select 1 from prdt_mapper pm where de.prdt_no=pm.prdt_no_old and opn_br_no='770088');";

        String updateAgaccseqn = " update de_mst de set de.ag_acc_seqn =  (select acc_seqn_new from ac_seqn_map asn where de.ag_acc_no=asn.acc_no_new " +
                " and de.ag_acc_seqn=asn.acc_seqn_old and opn_br_no='770088') where exists （select 1 from ac_seqn_map asn where " +
                " de.ag_acc_no=asn.acc_no_new and de.ag_acc_seqn=asn.acc_seqn_old and opn_br_no='770088');";

        //更新de_mst后 acc_id后插入acc_seqn表中记录 acc_seqn_max  因acc_id没有重复 最大值默认为 20001
        String updateAccSeqn = "insert into acc_seqn (acc_id,acc_seqn_type,acc_seqn_max)   select a.acc_id,b.acc_seqn_type,b.acc_seqn from DE_MST a ,(select ag_acc_no,'ST03' as acc_seqn_type,  max(acc_seqn) as acc_seqn from DE_MST " +
                " where opn_br_no='770088' and acc_seqn !=1 group by ag_acc_no union (select ag_acc_no,'ST00' as acc_seqn_type,  acc_seqn from DE_MST " +
                " where opn_br_no='770088' and acc_seqn =1)) b where a.ag_acc_no = b.ag_acc_no and  a.acc_seqn = b.acc_seqn;";

        //de_mst表数据更新完成之后执行
        String insertEcifPrdtSignInfo = "insert into ecif_sign_prdt_info select de_cif_no,cif_name,'CT01',prdt_no,NULL,100,ag_acc_no,acc_seqn,de_mtr_date,de_opn_date,NULL,'0061',NULL,ag_acc_no,1,NULL,NULL,'SS00',NULL,NULL,'BF01','770000' from " +
                " ((select de_cif_no,ag_acc_no,acc_seqn,prdt_no,de_mtr_date,de_opn_date from de_mst " +
                " where opn_br_no ='770088'  and acc_seqn != 1 and de_cif_no is not null) a left join icore_ecif_bth.cif_info_mid b on a.de_cif_no = b.cif_no_new);";

        stringBuilder.append(deInstDate+"\n");
//        stringBuilder.append(deHoldAmt+"\n");
        stringBuilder.append(updateAgaccno+"\n");
        stringBuilder.append(prdt_no+"\n");
        stringBuilder.append(updateAgaccseqn+"\n");
        stringBuilder.append(updateAccSeqn+"\n");
        stringBuilder.append(insertEcifPrdtSignInfo+"\n");


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
