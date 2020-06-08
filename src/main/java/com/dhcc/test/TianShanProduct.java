package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.entity.PrdtRelMapper;
import com.dhcc.entity.Product;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;

public class TianShanProduct {

    public static void main(String[] args) {
        String fileName = "十年庆3个.csv";
        List<CsvRow> rows =   MyCsvUtil.getData(fileName);
        rows.remove(0);
        List<Product> products =toProducts(rows);
        StringBuilder stringBuilder=new StringBuilder();
        StringBuilder 天山新旧产品对照表 = new StringBuilder("产品名称,旧的产品编号,现在产品编号,产品工厂编号\n");
        //获取旧的产品号和产品名称
        List<PrdtRelMapper> prdtMapperList = Prdtinforel.getPrdtMapperList();
        for (Product product: products) {
            //循环产品时,生成新旧产品对照信息
            for(PrdtRelMapper mapper:prdtMapperList){
                if(product.getPrdtName().equals(mapper.getPrdt_name())){
                    天山新旧产品对照表.append(product.getPrdtName()+","+mapper.getOld_prdt_no()+","+product.getPrdtNo()+","+product.getGcNo()+"\n");
                }
            }
            stringBuilder.append(getBaseSql(product));
        }
        //根据cvs文件生成产品配置参数
        MyCsvUtil.writFile(stringBuilder.toString(),fileName);
        //生成中间新旧产品对照cvs
        MyCsvUtil.writFile(天山新旧产品对照表.toString(),"天山新旧产品对照表",".csv");
    }


    public static List<Product> toProducts(List<CsvRow>  rows){
        List<Product> products=new ArrayList<>(rows.size());
        String chnnl;
        String prdtName;//        产品名称
        String prdtNo;//        分类
        String 产品类型;//        存款期
        String de_term;
        String de_termUnit;//        最低起存金额
        String opn_min_amt;//        发售期
        String fashouqi;//   发售期单位
        String fashouqiUnit;//        利率
        String rate;//      计息规则
        String rule;//        递增金额
        String opn_unit_amt;//        是否可自动转存
        String tfr_lmt_type;//        是否可部分提前支取
        String draw_adva_flag;
        String brNo;
        String gcNo;
        for (CsvRow csvRow : rows) {
            Object[] object = csvRow.toArray();
            chnnl = object[0].toString().trim();
            prdtName = object[1].toString().trim();
            prdtNo = object[2].toString().trim();
            gcNo = object[3].toString().trim();
            产品类型 = object[4].toString().trim();
            de_term = object[5].toString().trim();
            de_termUnit = object[6].toString().trim();
            opn_min_amt = object[7].toString().trim();
            fashouqi = object[8].toString().trim();
            fashouqiUnit = object[9].toString().trim();
            rate = object[10].toString().replace("%", "").trim();
            rule = object[11].toString().trim();

            opn_unit_amt = object[12].toString().trim();
            tfr_lmt_type = object[13].toString().trim();
            draw_adva_flag = object[14].toString().trim();
            brNo = object[15].toString().trim();
            Product product = new Product(chnnl, prdtName, prdtNo, gcNo, 产品类型, de_term,
                    de_termUnit, opn_min_amt, fashouqi, fashouqiUnit, rate, rule,
                    opn_unit_amt, tfr_lmt_type, draw_adva_flag, brNo);
            product.set存款类型(object[16].toString().trim());
            products.add(product);
        }

        return products;
    }


    public static StringBuilder getBaseSql(Product product) {
        StringBuilder stringBuilder = new StringBuilder();
        String chnnl=product.getChnnl();
        //        产品名称
        String prdtName=product.getPrdtName();
        //        产品分类
        String 产品类型=product.get产品类型();
        String 存款类型=product.get存款类型();
        //        存款期
        String de_term=product.getDe_term();
        //        最低起存金额
        String opn_min_amt=product.getOpn_min_amt();
        //        发售期
        String fashouqi=product.getFashouqi();
        //        利率
        String rate=product.getRate();
        //      计息规则
        String rule=product.getRule();
        //        递增金额
        String opn_unit_amt=product.getOpn_unit_amt();
        //        是否可自动转存
        String tfr_lmt_type=product.getTfr_lmt_type();
        //        是否可部分提前支取
        String draw_type=product.getDraw_type();
//        DE0161	"draw_adva_flag"	"提前支取标志"	"CT02"	"K004"	"PT02"	"0 不能提前支取；[1-9]提前支取次数；-1 不需配置该参数"
        String draw_adva_flag="0";
        if(draw_type.equals("DF00")){
            draw_adva_flag="9";
        }
        String brNo=product.getBrNo();
        String gcNo=product.getGcNo();
        String prdtNo=product.getPrdtNo();
        String de_TermUnit=product.getDe_termUnit();

        String basesql = "--产品协议没写\n" +
                "--"+prdtName+"\n" +
                "INSERT INTO PRDT_BASE_INFO (PRDT_NO, PRDT_NAME, PRDT_BELONG, PRDT_CUR, PRDT_OBJ, PRDT_PM, PRDT_DATE, PRDT_VER, PRDT_STS, PRDT_DESC, PRDT_AGMT_PATH, PRDT_APPR_TEL_NO, PRDT_APPR_TEL_NAME, PRDT_DESG_CERT_NO, PRDT_DESG_NAME, PRDT_SEQN_TYPE, PRDT_SELF_FLAG, PRDT_CIF_LVL, BR_NO) VALUES ('"+prdtNo+"', '"+prdtName+"', 'PB01', '156', 'PO01', '张俊生', '20181012', '1.0.0', 'PS04', '"+prdtName+"是一款高收益的存款产品，起存金额为"+opn_min_amt+"元（含），最小购买单位"+opn_unit_amt+"元；客户最大购买限额无；"+rule+"', null, null, null, null, '张俊生', 'ST03', 'PS00', 'LV01:1;LV02:1;LV03:1;LV04:1', '"+brNo+"');\n" +
                "INSERT INTO PRDT_CHNL_REL (PRDT_NO, CHNL_NO, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO) VALUES ('"+prdtNo+"', '0061', '19000101', '000000', '99991231', '000000', '"+brNo+"');\n" +
                "INSERT INTO PRDT_CIF_LVL_REL (PRDT_NO, CIF_LVL, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO) VALUES ('"+prdtNo+"', 'LV01', '20160101', '000000', '99999999', '999999', '"+brNo+"');\n" +
                "INSERT INTO PRDT_CIF_LVL_REL (PRDT_NO, CIF_LVL, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO) VALUES ('"+prdtNo+"', 'LV02', '20160101', '000000', '99999999', '999999', '"+brNo+"');\n" +
                "INSERT INTO PRDT_CIF_LVL_REL (PRDT_NO, CIF_LVL, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO) VALUES ('"+prdtNo+"', 'LV03', '20160101', '000000', '99999999', '999999', '"+brNo+"');\n" +
                "INSERT INTO PRDT_CIF_LVL_REL (PRDT_NO, CIF_LVL, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO) VALUES ('"+prdtNo+"', 'LV04', '20160101', '000000', '99999999', '999999', '"+brNo+"');\n" +
                "INSERT INTO PRDT_CIF_TYPE_REL (PRDT_NO, CIF_TYPE, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO) VALUES ('"+prdtNo+"', 'CT01', '20160101', '000000', '9999999', '999999', '"+brNo+"');\n" +
                "INSERT INTO PRDT_INFO_REL (PRDT_NO, PRDT_MOULD_KND, INFO_TYPE_SEQN, PRDT_MOULD_NO, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO, PRDT_MOULD_EXP, PRDT_MOULD_EXP_CRT) VALUES ('"+prdtNo+"', 'MK03', 1, '21', '20190208', '00:00:00', '20180808', '23:59:59', '"+brNo+"', null, null);\n" +
                "INSERT INTO PRDT_PROX_BR_REL (PRDT_NO, PBR_PRDT_NO, PBR_PRDT_NAME, PB_NO, PBR_PROT_TYPE, PBR_PROF_MODE, PBR_PROF_RADI, PBR_DESC, PBR_PROT_FILE, BR_NO) VALUES ('"+prdtNo+"', '"+gcNo+"', '"+prdtName+"', null, null, null, null, null, null, '"+brNo+"');\n" +
                "\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0002', null, null, '0', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0003', null, null, 'TT01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0004', null, null, '"+产品类型+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0005', null, null, '"+存款类型+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0006', null, null, '"+de_term+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0007', null, null, '"+de_TermUnit+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0008', null, null, 'DI01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0009', null, null, 'AD00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0010', null, null, '99991231', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0011', null, null, 'AI00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0051', null, null, '"+opn_unit_amt+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0052', null, null, '"+opn_min_amt+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0053', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0054', null, null, 'OC00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0055', null, null, 'BF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0056', null, null, 'BF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0057', null, null, 'BF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0058', null, null, 'BF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0059', null, null, 'BF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0061', null, null, 'MI00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0062', null, null, 'OS01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0101', null, null, 'DF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0102', null, null, '0.01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0103', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0104', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0105', null, null, '0.00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0106', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0107', null, null, 'IT02', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0108', null, null, 'CF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0109', null, null, 'TF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0110', null, null, 'DF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0151', null, null, 'DF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0152', null, null, '1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0153', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0154', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0155', null, null, '1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0156', null, null, '0', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0157', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0158', null, null, 'IT00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0159', null, null, 'DC00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0160', null, null, 'DT01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0161', null, null, '"+draw_adva_flag+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0162', null, null, 'DF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0163', null, null, 'TR00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0164', null, null, 'OF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0165', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0166', null, null, 'OT00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0167', null, null, '0.00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0168', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0169', null, null, '0.01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0170', null, null, '-1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0201', null, null, 'CZ01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0202', null, null, 'CC00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0203', null, null, 'CT01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0204', null, null, 'CA00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0205', null, null, 'PI00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0206', null, null, 'CF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0207', null, null, 'AF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0208', null, null, '0', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0209', null, null, 'HF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0251', null, null, 'IC01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0252', null, null, '00M00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0253', null, null, 'CC02', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0254', null, null, '00M00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0255', null, null, 'IT01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0256', null, null, 'AC01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0257', null, null, '1.00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0258', null, null, 'IT01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0259', null, null, 'IR01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0260', null, null, 'IF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0261', null, null, 'IO00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0262', null, null, 'IS01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0263', null, null, 'AC00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0301', null, null, 'RT01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0303', null, null, 'DE504', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0304', null, null, 'CT00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0305', null, null, '00M00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0306', null, null, 'OT00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0307', null, null, '"+rate+"', null, null, 'PS00', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0351', null, null, 'PF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0352', null, null, 'PT04', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0353', null, null, '00M28', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0354', null, null, 'PK00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0401', null, null, 'TF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0402', null, null, '100', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0403', null, null, 'SF00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0404', null, null, '0', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0405', null, null, 'BF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0406', null, null, 'SD00', null, null, 'PS00', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0407', null, null, 'HD00', null, null, 'PS00', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0408', null, null, 'OT02', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0409', null, null, 'DY00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0410', null, null, '20190130', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0411', null, null, '99991231', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0413', null, null, '20000000.00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0414', null, null, 'DC00', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0415', null, null, '20181012', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0416', null, null, 'MF01', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0417', null, null, '1', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0418', null, null, 'DT02', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0419', null, null, '"+tfr_lmt_type+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_PARM (PRDT_NO, PARM_NO, PARM_VAL_TYPE, PARM_VAL_MODE, PARM_VAL, PARM_BEG_DATE, PARM_END_DATE, PARM_STS, PARM_DESC, PARM_SHOW, BR_NO) VALUES ('"+prdtNo+"', 'DE0420', null, null, '"+draw_type+"', null, null, 'PS01', null, null, '"+brNo+"');\n" +
                "INSERT INTO PRDT_BR_REL (PRDT_NO, BR_NO, BEG_DATE, BEG_TIME, END_DATE, END_TIME) VALUES ('"+prdtNo+"', '"+brNo+"', '20190601', '000000', '20990101', '235959');\n"+
                "INSERT INTO PRDT_RECO (RE_NO, PRDT_CLS, POS_NO, PRDT_NO, DATE_BEG, DATE_END, RECO_WEIG, RE_STS, CRT_USER, CRT_DATE, RECO_TYPE, BR_NO, RECO_ID) VALUES ('2', 'P00034', null, '"+prdtNo+"' , '20170507', '20990507', 2, 'RS00', null, null, 'RT01', '"+brNo+"', '5');\n" +
                "INSERT INTO PRDT_MDM_REL (PRDT_NO, MDM_CODE, BEG_DATE, BEG_TIME, END_DATE, END_TIME, BR_NO) VALUES ('"+prdtNo+"', 'EM01', '20170304', '000000', '20990101', '235959', '"+prdtNo+"');\n";
        stringBuilder.append(basesql);
        return stringBuilder;
    }
}
