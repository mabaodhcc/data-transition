package com.dhcc.test;
import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.Ecif;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;

public class TianShanUserRegister01 {

    public static List<Ecif> toEcifList(List<CsvRow> rows) {
        List<Ecif> ecifs = new ArrayList<>(rows.size());
        for (CsvRow csvRow : rows) {
            Object[] array = csvRow.toArray();
            String string = (String)array[0];
            String[] object = string.split("\\|");

            String ssss = object[10].toString().trim();
            String 开户机构号 = "850000";
            String 流水号 = "";
            String 证件类型 = "ZJ01";
            String 证件号 = object[6].toString().trim();
            String 客户姓名 = object[7].toString().trim();
            String 手机号 = object[8].toString().trim();
            String 身份证起始日期 = "";
            String 身份证有效结束日期 = "";
            String 身份证注册地 = "";
            String 客户手机银行注册日期 = object[10].toString().trim();
            String 注册时间 = object[10].toString().trim();
            String 电子账号 = "";
            String 账户性质 = "";
            String 账户状态 = "";
            String 开户渠道 = "";
            String 核心客户号 = object[4].toString().trim();
            String 网银客户号 = object[12].toString().trim();

            Ecif ecif = new Ecif(开户机构号, 流水号, 证件类型, 证件号, 客户姓名, 手机号, 身份证起始日期, 身份证有效结束日期, 身份证注册地,
                    客户手机银行注册日期, 注册时间, 电子账号, 账户性质, 账户状态, 开户渠道, 核心客户号, 网银客户号);
            ecifs.add(ecif);
        }
        return ecifs;
    }
    public static void main(String[] args) {
        String fileName= "sj-个人客户信息表.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        List<Ecif> ecifs = toEcifList(rows);
        //注册信息表sql
        StringBuilder stringBuilder = new StringBuilder();
        //生成客户信息中间表.csv
        StringBuilder 客户信息中间表 = new StringBuilder("证件号,证件类型,客户姓名,核心客户号,网银客户号,客户号,手机号,注册时间\n");
        getSQL(ecifs,stringBuilder,客户信息中间表);
        MyCsvUtil.writFile(stringBuilder.toString(),fileName);
        MyCsvUtil.writFile(客户信息中间表.toString(),"客户信息中间表",".csv");

    }

    public static String getSQL(List<Ecif> rows,StringBuilder stringBuilder,StringBuilder 客户信息中间表) {
//        客户编码目前最大的值是41
        int start = 41;
//        方大的机构标识就是123
        String br_sign = "123";
//        映像编号目前最大值是42847
        int VI_NO = 42847;
        int increase = -1;
        for (Ecif row : rows) {
            increase+=1;
            System.out.println(increase);
            start = start + 1;
            String 新的客户编码 = "1" + br_sign + String.format("%08d", start);
            String 核心客户号 =row.get核心客户号();
            String 网银客户号 =row.get网银客户号();
            String 客户姓名 = row.get客户姓名();
            String 建立日期 = row.get客户手机银行注册日期();
            String 开户机构号 = Constant.机构号;
            String 手机号 = row.get手机号();
            String 证件类型 = row.get证件类型();
            String 证件号 = row.get证件号();
//           目前是随便取了个值
            String 登录密码 = "d41d8cd98f00b204e9800998ecf8427e";
            // 注册时间 ECIF_LOGIN_INFO
            String 注册时间 = row.get客户手机银行注册日期();
            String 上次登录号码 = 手机号;
            String 上次登录时间 = 注册时间;

            // 注册时间 ECIF_VEDIO_INFO
            String VI_UP_DATE="";
            String VI_UP_TIME="";
            if(row.get客户手机银行注册日期()!=null || !"".equals(row.get客户手机银行注册日期())){
                System.out.println(row.get客户手机银行注册日期());
                VI_UP_DATE = row.get客户手机银行注册日期().substring(0,8);
                VI_UP_TIME = row.get客户手机银行注册日期().substring(8,14);

            }
            String VI_UP_DEVICE = "未设置";

            //ECIF_BASE_INFO
            String ecif_base_info = "INSERT INTO ECIF_BASE_INFO (\"CIF_NO\", \"CIF_CN_NAME\", \"CIF_EN_NAME\", \"CIF_PY_NAME\", \"CIF_SN_NAME\", \"CIF_TYPE\", \"CIF_LOCAL_FLAG\", \"CIF_POTEN_FLAG\", \"CIF_EMP_FLAG\", \"CIF_STOCK_FLAG\", \"CIF_LVL\", \"CIF_CRED_LVL\", \"CIF_INFO\", \"CIF_STS\", \"CIF_CTB_LVL\", \"CIF_RISK_LVL\", \"CIF_RISK_ALERT_SIG\", \"CIF_LVL_MOD_TYPE\", \"CIF_LVL_WARN_NUM\", \"CIF_ADD_INFO1\", \"CIF_ADD_INFO2\", \"CIF_CRT_BR_NO\", \"CIF_CRT_OPER\", \"CIF_CRT_DATE\", \"CIF_MOD_DATE\", \"BR_NO\") " +
                    "VALUES ('" + 新的客户编码 + "', '" + 客户姓名 + "', NULL, NULL, NULL, 'CT01', 'LF00', NULL, 'EF00', NULL, 'LV01', 'CL01', NULL, 'CS00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '" + VI_UP_DATE + "', NULL, '" + 开户机构号 + "');\n";

            //ECIF_LOGIN_INFO
            String ecif_login_info = "INSERT INTO ECIF_LOGIN_INFO (\"LOGIN_NAME\", \"PHONE_NO\", \"CERT_TYPE\", \"CERT_NO\", \"CIF_EMAIL\", \"PWD_GPWD_FLAG\", \"PWD_GPWD\", \"PWD_GPWD_ERR_CNT\", \"PWD_GPWD_ERR_TIME\", \"PWD_LOGIN\", \"PWD_LOGIN_ERR_CNT\", \"PWD_LOGIN_ERR_TIME\", \"REG_DATE\", \"LAST_LOGIN_MODE\", \"LAST_LOGIN_NO\", \"LAST_LOGIN_TIME\", \"LAST_LOGOUT_TIME\", \"LOGIN_CHNL_CODE\", \"LOGIN_IP\", \"LOGIN_DEVICE\", \"LOGIN_DESC\", \"LOGIN_STS\", \"BR_NO\", \"USER_ID\")" +
                    " VALUES ('" + 客户姓名 + "', '" + 手机号 + "', '" + 证件类型 + "', '" + 证件号 + "', NULL, NULL, NULL, NULL, NULL, '" + 登录密码 + "', NULL, NULL, '" + 注册时间 + "', NULL, '" + 上次登录号码 + "', '" + 上次登录时间 + "', NULL, NULL, NULL, NULL, NULL, NULL, '" + 开户机构号 + "', NULL);\n";

            //ECIF_VEDIO_INFO
            /*//每个用户需要插入3条数据的
            StringBuilder ecif_vedio_info = new StringBuilder();
            for (int i = 1; i <= 3; i++) {
                VI_NO = VI_NO + 1;
                String VI_NAME = null;
                if (i == 1) {
                    VI_NAME = "cert_front";
                } else if (i == 2) {
                    VI_NAME = "cert_hand";
                } else if (i == 3) {
                    VI_NAME = "cert_reverse";
                }
                String item = "INSERT INTO ECIF_VEDIO_INFO (\"VI_NO\", \"CIF_NO\", \"CIF_ACT\", \"VI_PATH\", \"VI_TYPE\", \"VI_SIZE\", \"VI_UP_DATE\", \"VI_UP_TIME\", \"VI_OPER\", \"VI_VALID_DATE\", \"VI_UP_DEVICE\", \"VI_STS\", \"VI_NAME\", \"BR_NO\", \"CERT_TYPE\", \"CERT_NO\") " +
                        "VALUES ('" + VI_NO + "', '" + 新的客户编码 + "', 'CA01', NULL, 'VT00', NULL, '" + VI_UP_DATE + "', '" + VI_UP_TIME + "', NULL, '29991230', '" + VI_UP_DEVICE + "', 'VS00', '" + VI_NAME + "', '" + 开户机构号 + "', '" + 证件类型 + "', '" + 证件号 + "');\n";
                ecif_vedio_info.append(item);
            }*/

            //ECIF_CERT_INFO
            String 身份证起始日期 = row.get身份证起始日期();
            String 身份证有效结束日期 = row.get身份证有效结束日期();
            String 身份证注册地 = row.get身份证注册地();
            String ecif_cert_info = "INSERT INTO ECIF_CERT_INFO (\"CIF_NO\", \"CERT_TYPE\", \"CERT_NO\", \"CERT_NAME\", \"CERT_DESC\", \"CERT_LIC\", \"CERT_CHK\", \"CERT_NET_FLAG\", \"CERT_SUPT\", \"CERT_MAIN_FLAG\", \"CERT_STS\", \"CERT_BEG_DATE\", \"CERT_END_DATE\", \"CERT_LAST_DATE\", \"BR_NO\")" +
                    " VALUES ('" + 新的客户编码 + "', '" + 证件类型 + "', '" + 证件号 + "', '" + 客户姓名 + "', NULL, '" + 身份证注册地 + "', 'CC00', 'NF01', 'CS00', 'MF01', 'CS00', NULL, NULL, NULL, '" + 开户机构号 + "');\n";

            //ECIF_PER_INFO
            String ecif_per_info = "INSERT INTO ECIF_PER_INFO(\"CIF_NO\", \"PER_NAT_CODE\", \"PER_LIVE_CODE\", \"PER_LANG_CODE\", \"PER_RACE_CODE\", \"PER_SUN_BIRTH\", \"PER_MOON_BIRTH\", \"PER_SEX\", \"PER_EDU\", \"PER_RELI\", \"PER_CATA\", \"PER_PROF\", \"PER_DUTY\", \"PER_TITLE\", \"PER_INCOME\", \"PER_HOME_INCOME\", \"PER_MARRY\", \"PER_MARRY_DATE\", \"PER_FARMER\") " +
                    "VALUES ('" + 新的客户编码 + "', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);\n";

            //ECIF_PHOTO_INFO
            String ecif_photo_info = "INSERT INTO ECIF_PHOTO_INFO (\"CIF_NO\", \"CIF_PHOTO\", \"CIF_BIG_PHOTO\", \"CIF_UP_DATE\", \"CIF_UP_TIME\", \"BR_NO\") " +
                    "VALUES ('" + 新的客户编码 + "', NULL, NULL, '" + VI_UP_DATE + "', '" + VI_UP_TIME + "', '" + 开户机构号 + "');\n";
            String ecif_per_cont_info = "INSERT INTO ECIF_PER_CONT_INFO (\"CIF_NO\", \"CONT_HOME_ADDR\", \"CONT_NOW_ADDR\", \"CONT_POST\", \"CONT_HOME_PHONE\", \"CONT_PHONE\", \"CONT_HOME_FAX\", \"CONT_URL\", \"CONT_EMAIL\", \"CONT_QQ\", \"CONT_SKYPE\", \"CONT_MSN\", \"CONT_WECHAT\", \"CONT_BLOG\", \"CONT_ALIAS\") " +
                    "VALUES ('" + 新的客户编码 + "', NULL, NULL, NULL, NULL, '" + 手机号 + "', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);\n";
            String ecif_pwd_info = "INSERT INTO ECIF_PWD_INFO (\"CIF_NO\", \"PWD_SEC_LVL\", \"PWD_CHNG_INTE\", \"PWD_PAY_LAST_DATE\", \"PWD_QUERY_LAST_DATE\", \"PWD_PAY_ERR_CNT\", \"PWD_QUERY_ERR_CNT\", \"PWD_ERR_TOL_CNT\", \"PWD_DESC\") " +
                    "VALUES ('" + 新的客户编码 + "', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);\n";

            stringBuilder.append(ecif_base_info)
                    .append(ecif_login_info)
//                    .append(ecif_vedio_info)
                    .append(ecif_cert_info)
                    .append(ecif_per_info)
                    .append(ecif_photo_info)
                    .append(ecif_per_cont_info)
                    .append(ecif_pwd_info)
                    .append("\n");
            String temp=证件号+","+证件类型+","+客户姓名+","+核心客户号+","+网银客户号+","+新的客户编码+","+手机号+","+注册时间+"\n";
            客户信息中间表.append(temp);
        }
        stringBuilder.append("UPDATE ECIF_NO_CTL set cif_no_seqn = '" + start + "' where br_no = '"+Constant.机构号+"' and cif_type = 'CT01';\n");

        return stringBuilder.toString();
    }
}