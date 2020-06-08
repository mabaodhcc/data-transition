package com.dhcc.test;


import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.entity.EcifLoginLog;
import com.dhcc.entity.Product;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;

public class 客户登录日志 {

    public static void main(String[] args) {
        String fileName = "个人登陆日志.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        List<EcifLoginLog> ecifLoginLogs =toProducts(rows);
        StringBuilder stringBuilder=new StringBuilder();
        for (EcifLoginLog ecifLoginLog: ecifLoginLogs) {
            if ("logonTransService".equals(ecifLoginLog.get服务Id())) {
                stringBuilder.append(getBaseSql(ecifLoginLog));
            }
        }
        MyCsvUtil.writFile(stringBuilder.toString(),fileName);
    }
    public static List<EcifLoginLog> toProducts(List<CsvRow>  rows){
        List<EcifLoginLog> ecifLoginLogs=new ArrayList<>(rows.size());
        String 网银客户号;
        String 登录时间;
        String 服务Id;
        String log_state;
        String 日志信息;
        for (CsvRow csvRow : rows) {
            Object[] object1 = csvRow.toArray();
            String s = object1[0].toString();
            String[] object = s.split("\\|");
            网银客户号 = object[0].toString().trim();
            登录时间 = object[1].toString().trim();
            服务Id = object[2].toString().trim();
            log_state = object[3].toString().trim();
            日志信息 = object[4].toString().trim();
            EcifLoginLog ecifLoginLog = new EcifLoginLog(网银客户号,登录时间,服务Id,log_state,日志信息);
            ecifLoginLogs.add(ecifLoginLog);
        }

        return ecifLoginLogs;
    }
    public static StringBuilder getBaseSql(EcifLoginLog ecifLoginLog) {
        StringBuilder stringBuilder = new StringBuilder();
        String 网银客户号=ecifLoginLog.get网银客户号();
        String 登录时间=ecifLoginLog.get登录时间();
        String 服务Id=ecifLoginLog.get服务Id();
        String log_state=ecifLoginLog.getLog_state();
        String 日志信息=ecifLoginLog.get日志信息();
        String [] str = 日志信息.split("，");
        String ip = "";
        String device = "";
        if (str.length>3) {
            ip = str[3].replace("IP:", "");
        }
        if (str.length>4){
            device = str[4].replace("MAC地址:","");
        }

        String sql = "(select max(seqn)+1 from ECIF_LOGIN_LOG)";
        //ECIF_LOGIN_LOG
        String basesql = "INSERT INTO ECIF_LOGIN_LOG (\"SEQN\", \"CIF_NO\", \"COP_OPER_NO\", \"LOGIN_CHNL\", \"LOGIN_NO\", \"LOGIN_IP\", \"LOGIN_AREA\", \"LOGIN_TIME\", \"LOGIN_OUT_TIME\", \"LOGIN_DEVICE\", \"LOGIN_DESC\", \"TX_BR_NO\") " +
                "VALUES (" + sql + ", '" + 网银客户号 + "', NULL, NULL, NULL, '"+ip+"', NULL, '" + 登录时间 + "', NUll, '"+device+"', '注册时的登陆', '770088' );\n";


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
