/*
package com.dhcc.test;


import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.EcifTemp;
import com.dhcc.entity.ProductRel;
import com.dhcc.entity.SubAccount;
import com.dhcc.util.MyCsvUtil;
import com.dhcc.util.TempDateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class 获得子账户04 {
    public static void main(String[] args) {
        String fileName="子账户.csv";
        List<CsvRow> list = MyCsvUtil.getData(fileName);
        list.remove(0);
        List<SubAccount> subAccounts=toSubAccount(list);
//        这里首先要从数据库PRDT_PROX_BR_REL中导出csv文件存到c盘下，文件名称：数据库里的产品关系表.csv（表头："产品编号","产品工厂编号","产品名称"）
//        SELECT p.PRDT_NO 产品编号,p.PBR_PRDT_NO  产品工厂编号,p.PBR_PRDT_NAME  产品名称 from PRDT_PROX_BR_REL  p
        HashMap<String, ProductRel> productRels = TempDateUtil.getProductRels();
        HashMap<String, EcifTemp> ecifTempsHashMap = TempDateUtil.getEcifTemps();
        List<EcifTemp> ecifTemps= new ArrayList<>(ecifTempsHashMap.values());

        StringBuilder stringBuilder=new StringBuilder();
        StringBuilder 新旧产品对照表 = new StringBuilder("产品名称,旧的产品编号,现在产品编号,产品工厂编号\n");
//        HashSet<ProductRel> productRelHashSet=new HashSet<>();
        for (SubAccount subAccount: subAccounts) {
            String 产品名称=subAccount.get产品名称();
            ProductRel productRel=productRels.get(产品名称);
//            直接从行里提供的csv数据的客户号、电子账户都为旧的客户号，电子账户
            String 旧的客户号 = subAccount.get客户号();
           Optional<EcifTemp> ecifTempOptional = ecifTemps.stream().filter(p->p.get旧的客户号().equals(旧的客户号)).findFirst();
            if(productRel==null||!ecifTempOptional.isPresent()){
                continue;
            }
            EcifTemp ecifTemp =ecifTempOptional.get();
//            产品编号要从中间表中取
            subAccount.set现在的产品号(productRel.get现在产品编号());
//            后面需要修改真是数据，暂时前4列就先直接从csv里面取值
//            因为ACC_id和AG_ACC_NO都是从介质账户表中取来的，所以从中间表文件中取值
            String 内部账号=ecifTemp.get介质账户ACC_ID();
//            "AG_ACC_NO",购卡号,就是电子账户
            String 新的电子账户=ecifTemp.get新的电子账户();
            //"AG_ACC_SEQN",账户序号
//            String 账户序号=subAccount.get账户序号();
            String 新的账户序号=ecifTemp.get新的账户序号();
//          acc_seqn  内部账号序号
            String acc_seqn="未设置";
//            ACC_SEQN， 这个值的获取可能有问题 待改善 未设置
            String 合作机构代码= Constant.机构号;
            String 利率=subAccount.get利率();
            String 起息日期=subAccount.get起息日();
            String 现在的产品号=subAccount.get现在的产品号();
            String 账户余额=subAccount.get账户余额();
            String 最后动账日=subAccount.get最后动账日();
            String 透支标志=subAccount.get透支标志();
            String 账户状态=subAccount.get账户状态();
            String 止付状态=subAccount.get止付状态();
            String 冻结状态=subAccount.get冻结状态();
            //最后动账日= 上笔发生日
            String 上笔发生日期=最后动账日;
            String 累计收益=subAccount.get累计收益();
            String 预计收益=subAccount.get预计收益();
            String 提现标识=subAccount.get提现标识();
            String 开户时间=subAccount.get签约日期();//只要日期部分
            String 冻结金额=subAccount.get冻结金额();
            String 止付金额=subAccount.get止付金额();
            String 控制金额=subAccount.get控制金额();
//            String 结息日期=subAccount.get终止日();
//            结息日期= 上次结息日
            String 结息日期=subAccount.get上次结息日();
            String 开通标志="0000000000";
            String 客户号=subAccount.get客户号();
            String 账户类型="AT01";
            String 明细笔数="0";
            String de_mst="INSERT INTO  DE_MST (\"ACC_ID\", \"AG_ACC_NO\", \"AG_ACC_SEQN\", \"ACC_SEQN\", \"OPN_BR_NO\", \"AG_BR_NO\", \"PRDT_NO\", \"CUR_NO\", \"DE_BAL\", \"DE_BOOK_BAL\", \"DE_YS_BAL\", \"DE_RATE\", " +
                    "\"DE_HST_CNT\", \"DE_OPN_DATE\", \"DE_IC_DATE\", \"DE_LAST_DATE\", \"DE_MTR_DATE\", \"DE_ACC_STS\", \"DE_HOLD_STS\", \"DE_HOLD_AMT\", \"DE_STOP_PAY_STS\", \"DE_STOP_PAY_AMT\", \"DE_CTL_AMT\", \"DE_OD_FLAG\", \"DE_CIF_NO\", \"DE_OPEN_FLAG\"," +
                    " \"DE_MAC\", \"DE_INTS_DATE\", \"DE_CIF_LAST_DATE\", " +
                    "\"DE_RATE_TYPE\", \"DE_CASH_FLAG\", \"DE_ACC_TYPE\", \"DE_INTST\", \"DE_UNPAY_INTST\", \"DE_AMT1\", \"DE_AMT2\")" +
                    " VALUES ('"+内部账号+"', '"+新的电子账户+"', '"+新的账户序号+"', '"+acc_seqn+"', '"+合作机构代码+"', '"+合作机构代码+"', '"+现在的产品号+"', '156', '"+账户余额+"', '0', '0', '"+利率+"', " +
                    "'"+明细笔数+"', '"+开户时间+"', '"+起息日期+"', '"+上笔发生日期 +"', '99991231', '"+账户状态+"', '"+冻结状态+"', '"+冻结金额+"', '"+止付状态+"', '"+止付金额+"', '"+控制金额+"', '"+透支标志+"', " +
                    "'"+客户号+"', '"+开通标志+"', NULL, '"+结息日期+"', '"+最后动账日+"', 'RT00', '"+提现标识+"', '"+账户类型+"', " +
                    "'"+累计收益+"', NULL, NULL, NULL);\n";
            stringBuilder.append(de_mst);
//            productRelHashSet.add(new ProductRel( subAccount.get旧的产品号(),  subAccount.get现在的产品号(),  产品名称,  productRel.get产品工厂编号()));
            新旧产品对照表.append(产品名称+","+subAccount.get旧的产品号()+","+subAccount.get现在的产品号()+","+productRel.get产品工厂编号()+"\n");

        }
        //子账户表
        MyCsvUtil.writFile(stringBuilder.toString(),fileName);
        //保存新旧产品对照表临时数据
        MyCsvUtil.writFile(新旧产品对照表.toString(),"新旧产品对照表",".csv");

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

            SubAccount subAccount=new SubAccount(客户号,电子账号,账户序号,旧的产品号,开户时间,起息日,终止日,
                    上次结息日,产品名称,账户余额,冻结状态,冻结金额,止付状态,止付金额,控制金额,
                    利率,积数,最后动账日,累计收益,预计收益,透支标志,
                    开户利率,账户状态,提现标识,签约状态,客户经理,签约日期,
                    开户金额,开户渠道);
            subAccounts.add(subAccount);
        }
        return subAccounts;
    }
}
*/
