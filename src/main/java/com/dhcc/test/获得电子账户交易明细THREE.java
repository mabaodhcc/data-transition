package com.dhcc.test;


import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.TradeDetail;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;


public class 获得电子账户交易明细THREE {
    public static void main(String[] args) {
        String fileName = "202004.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        List<TradeDetail> tradeDetails = toTradeDetail(rows);
        StringBuilder stringBuilder = new StringBuilder();
        for (TradeDetail tradeDetail : tradeDetails) {
            String 主机流水号 = tradeDetail.get主机流水号();
            String 流水笔次 = tradeDetail.get流水笔次();
            String 开户机构 = tradeDetail.get开户机构();
            String 账户序号 = tradeDetail.get账户序号();
            String 账号 = tradeDetail.get账号();
            String 户名 = tradeDetail.get户名();
            String 产品编码 = tradeDetail.get产品编码();
            String 交易码 = tradeDetail.get交易码();
            String 增减标志 = tradeDetail.get增减标志();
            String 现转标志 = tradeDetail.get现转标志();
            String 交易金额 = tradeDetail.get交易金额();
            String 余额 = tradeDetail.get余额();
            String 交易日期 = tradeDetail.get交易日期();
            String 机器日期 = tradeDetail.get机器日期();
            String 交易时间 = tradeDetail.get交易时间();
            String 摘要代码 = tradeDetail.get摘要代码();
            String 摘要 = tradeDetail.get摘要();
            String 对方账号 = tradeDetail.get对方账号();
            String 对方名称 = tradeDetail.get对方名称();
            String 交易订单号 = tradeDetail.get交易订单号();
            String 明细状态 = tradeDetail.get明细状态();
            String 场景类型 = tradeDetail.get场景类型();
            String sql = "(select ACC_NO_NEW from AC_SEQN_MAP where ACC_NO_OLD = '"+账号+"' and ACC_SEQN_NEW='1')";
            //DE_MST_HST
            String basesql = "INSERT INTO DE_MST_HST (\"TRC_NO\", \"TRC_CNT\", \"OPN_BR_NO\", \"ACC_ID\", \"ACC_SEQN\"," +
                    " \"ACC_NO\", \"ACC_NAME\", \"PRDT_NO\", \"TX_CODE\", \"SUB_STEP_NAME\", \"DE_ADD_FLAG\"," +
                    " \"DE_CT_FLAG\", \"DE_TX_AMT\", \"DE_BOOK_BAL\", \"DE_BAL\", \"DE_INTS_ACM\", \"DE_TX_DATE\", " +
                    "\"DE_NAT_DATE\", \"DE_TX_TIME\", \"DE_NOTE_TYPE\", \"DE_NOTE_NO\", \"DE_HST_CNT\", \"DE_MSR_FLAG\"," +
                    " \"DE_PWD_FLAG\", \"DE_BRF_CODE\", \"DE_BRF\", \"DE_OPER\", \"DE_CHK\", \"DE_AUTH\", \"DE_OPER_TRC_NO\"," +
                    " \"DE_ACC_NO\", \"DE_ACC_ID\",\"DE_ACC_SEQN\", \"DE_ACC_NAME\", \"DE_BR_NO\", \"DE_AGT_CERT_TYPE\", \"DE_AGT_CERT_NO\"," +
                    " \"DE_AGT_CERT_NAME\", \"DE_CHNL_TYPE\", \"DE_CHNL_DATE\", \"DE_CHNL_TRC_NO\", " +
                    "\"DE_PRDT_TRC_NO\", \"DE_TX_ADDR\", \"DE_BUS_TYPE\", \"DE_AUTH_CODE\", \"DE_RECE_NO\", \"DE_PRDT_DATE\", " +
                    "\"AG_ACC_NO\", \"AG_ACC_SEQN\", \"DE_INTS\", \"DE_TAX_INTS\", \"DE_FEE_AMT\", \"DE_ID_FLAG\", \"DE_OD_NO\"," +
                    " \"DE_STS\", \"SCENE_TYPE\", \"IMP_CHNL_TYPE\", \"RECO_DATE\", \"RECO_TRC_NO\", \"DE_PRDT_TRC_CNT\") " +
                    "VALUES ("+主机流水号+", "+流水笔次+", '"+ Constant.tx_br_no+"', 9999, 1, "+sql+", '"+户名+"', '"+产品编码+"', '"+交易码+"'," +
                    " NULL, '"+增减标志+"', '"+现转标志+"' , "+交易金额+", "+余额+","+余额+", 0, '"+交易日期+"', '"+机器日期+"'," +
                    " '000000', NULL, NULL, 1, NULL,'PF00',NULL,'"+摘要+"', NULL,NULL,NULL,NULL,'"+对方账号+"',NULL,NULL," +
                    "'"+对方名称+"',NULL,NULL,NULL,NULL,'0061',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,"+sql+",1,NULL,NULL," +
                    "NULL,NULL,NULL,'"+明细状态+"',NULL,NULL,NULL,NULL,NULL);\n";


            stringBuilder.append(basesql);
        }
        String updSql = "update de_mst_hst a set ACC_ID=(select b.acc_id from mdm_acc_rel b where a.ACC_NO=b.ACC_NO and a.OPN_BR_NO='"+Constant.tx_br_no+"')" +
                "where exists (select 1 from mdm_acc_rel b where a.ACC_NO=b.ACC_NO and a.OPN_BR_NO='"+Constant.tx_br_no+"')";
        stringBuilder.append(updSql);
        MyCsvUtil.writFile(stringBuilder.toString(),fileName);
    }

    public static List<TradeDetail> toTradeDetail(List<CsvRow> rows) {
        List<TradeDetail> tradeDetails = new ArrayList<>(rows.size());
        for (CsvRow row : rows) {
            Object[] object = row.toArray();
            String 主机流水号 = object[0].toString().trim();
            String 流水笔次 = object[1].toString().trim();
            String 开户机构 = object[2].toString().trim();
            String 账户序号 = object[3].toString().trim();
            String 账号 = object[4].toString().trim();
            String 户名 = object[5].toString().trim();
            String 产品编码 = object[6].toString().trim();
            String 交易码 = object[7].toString().trim();
            String 增减标志 = object[8].toString().trim();
            String 现转标志 = object[9].toString().trim();
            String 交易金额 = object[10].toString().trim();
            String 余额 = object[11].toString().trim();
            String 交易日期 = object[12].toString().trim();
            String 机器日期 = object[13].toString().trim();
            String 交易时间 = object[14].toString().trim();
            String 摘要代码 = object[15].toString().trim();
            String 摘要 = object[16].toString().trim();
            String 对方账号 = object[17].toString().trim();
            String 对方名称 = object[18].toString().trim();
            String 交易订单号 = object[19].toString().trim();
            String 明细状态 = object[20].toString().trim();
            String 场景类型 = object[21].toString().trim();
            TradeDetail tradeDetail = new TradeDetail(主机流水号, 流水笔次, 开户机构, 账户序号, 账号, 户名, 产品编码, 交易码, 增减标志, 现转标志, 交易金额, 余额, 交易日期, 机器日期, 交易时间, 摘要代码, 摘要, 对方账号, 对方名称, 交易订单号, 明细状态, 场景类型);
            tradeDetails.add(tradeDetail);
        }
        return tradeDetails;
    }
}
