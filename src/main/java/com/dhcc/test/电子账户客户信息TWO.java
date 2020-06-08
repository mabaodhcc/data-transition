package com.dhcc.test;


import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.entity.MdmAccRel;
import com.dhcc.entity.Product;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;

public class 电子账户客户信息TWO {

    public static void main(String[] args) {
        String fileName = "过渡电子账户部分客户信息.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        List<MdmAccRel> mdmAccRels =toProducts(rows);
        StringBuilder stringBuilder=new StringBuilder();
        long i = 100000082568L;
        for (MdmAccRel mdmAccRel: mdmAccRels) {
            long seqn = i;
            stringBuilder.append(getBaseSql(mdmAccRel,seqn));
            i=++i;
        }
        String sql1 = "update mdm_acc_rel a set ACC_NO=(select b.ACC_NO_NEW from AC_SEQN_MAP b where a.acc_no=b.ACC_NO_OLD and b.ACC_SEQN_NEW='1')" +
                " where exists (select 1 from AC_SEQN_MAP b where a.acc_no=b.ACC_NO_OLD and b.ACC_SEQN_NEW='1');\n";
        stringBuilder.append(sql1);
        String sql = "update ACC_ID_SEQN set ACC_ID_SEQN=(select max(acc_id) from mdm_acc_rel) where ACC_ID_TYPE='IT00';";
        stringBuilder.append(sql);
        //两次用到这个文件
        MyCsvUtil.writFile(stringBuilder.toString(),fileName+"01");
    }
    public static List<MdmAccRel> toProducts(List<CsvRow>  rows){
        List<MdmAccRel> mdmAccRels=new ArrayList<>(rows.size());
        String 开户机构号;
        String 流水号;
        String 证件类型;
        String 证件号;
        String 客户姓名;
        String 手机号;
        String 身份证起始日期;
        String 身份证有效结束日期;
        String 身份证注册地;
        String 客户手机银行注册日期;
        String 注册时间;
        String 客户注册预留信息;
        String 电子账号;
        String 账户性质;
        String 账户状态;
        String 开户渠道;
        String 该客户系统内唯一编号;
        for (CsvRow csvRow : rows) {
            Object[] object = csvRow.toArray();
            开户机构号 = object[0].toString();
            流水号 = object[1].toString();
            证件类型 = object[2].toString();
            证件号 = object[3].toString();
            客户姓名 = object[4].toString();
            手机号 = object[5].toString();
            身份证起始日期 = object[6].toString();
            身份证有效结束日期 = object[7].toString();
            身份证注册地 = object[8].toString();
            客户手机银行注册日期 = object[9].toString();
            注册时间 = object[10].toString();
            客户注册预留信息 = object[0].toString();
            电子账号 = object[11].toString();
            账户性质 = object[12].toString();
            账户状态 = object[13].toString();
            开户渠道 = object[14].toString();
            该客户系统内唯一编号 = object[15].toString();
            MdmAccRel mdmAccRel = new MdmAccRel(开户机构号, 流水号, 证件类型, 证件号, 客户姓名, 手机号, 身份证起始日期, 身份证有效结束日期, 身份证注册地, 客户手机银行注册日期, 注册时间, 客户注册预留信息, 电子账号, 账户性质, 账户状态, 开户渠道, 该客户系统内唯一编号);
            mdmAccRels.add(mdmAccRel);
        }

        return mdmAccRels;
    }
    public static StringBuilder getBaseSql(MdmAccRel mdmAccRel,long seqn) {
        StringBuilder stringBuilder = new StringBuilder();
        String 开户机构号 = mdmAccRel.get开户机构号();
        String 流水号 = mdmAccRel.get流水号();
        String 证件类型 = mdmAccRel.get证件类型();
        String 证件号 = mdmAccRel.get证件号();
        String 客户姓名 = mdmAccRel.get客户姓名();
        String 手机号 = mdmAccRel.get手机号();
        String 身份证起始日期 = mdmAccRel.get身份证起始日期();
        String 身份证有效结束日期 = mdmAccRel.get身份证有效结束日期();
        String 身份证注册地 = mdmAccRel.get身份证注册地();
        String 客户手机银行注册日期 = mdmAccRel.get客户手机银行注册日期();
        String 注册时间 = mdmAccRel.get注册时间();
        String 电子账号 = mdmAccRel.get电子账号();
        String 账户性质 = mdmAccRel.get账户性质();
        String 账户状态 = mdmAccRel.get账户状态();
        String 开户渠道 = mdmAccRel.get开户渠道();
        String 该客户系统内唯一编号 = mdmAccRel.get该客户系统内唯一编号();
        String sql = "(select CIF_NO_NEW from icore_ecif_bth.CIF_INFO_MID where cert_no = '"+证件号+"')";
        //String sql1 = "(select ACC_NO_NEW from AC_SEQN_MAP where ACC_NO_OLD = '"+电子账号+"' and ACC_SEQN_NEW='1')";
        String sql2 = "(select max(acc_id)+1 from mdm_acc_rel)";

        //MDM_ACC_REL
        String basesql = "INSERT INTO MDM_ACC_REL (\"OPN_BR_NO\", \"ACC_NO\", \"ACC_ID\", \"ACC_SEQN\", \"ACC_NAME\", \"CIF_NO\", \"MDM_CODE\", \"NOTE_NO\", \"MDM_STS\", \"COLL_STS\", \"REL_BEG_DATE\", \"REL_END_DATE\", \"PAY_USE_PWD_FLAG\", \"PAY_USE_CERT_FLAG\", \"CERT_TYPE\", \"CERT_NO\", \"PAY_USE_CIPHER_FLAG\", \"MDM_MAIN_FLAG\", \"MDM_MAC\", \"MDM_OPEN_FLAG\", \"MDM_IC_INFO\", \"MDM_EXT_STS\") " +
                "VALUES ('770088', '"+电子账号+"', "+sql2+", 9999, '"+客户姓名+"', "+sql+", 'XN01', NULL, '"+账户状态+"', 'CS01', '"+客户手机银行注册日期+"', '99999999' , 'UF01', 'UF00', '"+证件类型+"', '"+证件号+"', 'UF00', '9999', NULL, '01', NULL, NULL);\n";


        stringBuilder.append(basesql);
        return stringBuilder;
    }

    public static StringBuilder getPRDT_PROX_BR_REL(Product product){
        String prdtNo=product.getPrdtNo();
        String gcNo=product.getGcNo();
        String prdtName=product.getPrdtName();
        String brNo=product.getBrNo();
        String str="INSERT INTO PRDT_PROX_BR_REL (PRDT_NO, PBR_PRDT_NO, PBR_PRDT_NAME, PB_NO, PBR_PROT_TYPE, PBR_PROF_MODE, PBR_PROF_RADI, PBR_DESC, PBR_PROT_FILE, BR_NO) VALUES ('"+prdtNo+"', '"+gcNo+"', '"+prdtName+"', null, null, null, null, null, null, '"+brNo+"');\n";
        StringBuilder stringBuilder=new StringBuilder(str);
    return stringBuilder;
    }

    public static StringBuilder getPRDT_MDM_RELandPRDT_RECO(Product product){
        String prdtNo=product.getPrdtNo();
        String gcNo=product.getGcNo();
        String prdtName=product.getPrdtName();
        String brNo=product.getBrNo();
        String str=
//                "INSERT INTO PRDT_RECO (RE_NO, PRDT_CLS, POS_NO, PRDT_NO, DATE_BEG, DATE_END, RECO_WEIG, RE_STS, CRT_USER, CRT_DATE, RECO_TYPE, BR_NO, RECO_ID) VALUES ('2', 'P00034', null, '"+prdtNo+"', , '20170507', '20990507', 2, 'RS00', null, null, 'RT01', '"+brNo+"', '5');\n";
                   "INSERT INTO PRDT_MDM_REL (PRDT_NO, MDM_CODE, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO) VALUES ('"+prdtNo+"', 'EM01', '20170304', '000000', '20990101', '235959', '"+prdtNo+"');\n";
        StringBuilder stringBuilder=new StringBuilder(str);
        return stringBuilder;
    }
}
