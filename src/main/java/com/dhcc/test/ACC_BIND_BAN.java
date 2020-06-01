package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.entity.AccBindBan;
import com.dhcc.util.MyCsvUtil;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-20 14:53
 * @version: 1.0.0
 */
public class ACC_BIND_BAN {
    public static void main(String[] args) {
        String fileName= "20200429-电子账户绑定本行卡.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        //拼接sql的容器
        StringBuilder sql = new StringBuilder();
        List<AccBindBan> accBindBans = csv2List(rows);
        getSql(sql,accBindBans);
        MyCsvUtil.writFile(sql.toString(),fileName);

    }

    public static void getSql(StringBuilder sql, List<AccBindBan> accBindBans){

        String table = "ACC_BIND_REL";
        for (AccBindBan accBindBan : accBindBans){
            sql.append("insert into "+table+" (ACC_NO,ACC_ID,ACC_SEQN,ACC_NAME,BND_ACC_TYPE,BND_ACC_NO,BND_ACC_NAME,BND_DATE,BND_CERT_TYPE,BND_CERT_NO,BND_PHONE_NO,BND_BANK_FLAG,BND_AUTH_FLAG,BND_STS,BR_NO) values(");
            sql.append("(select distinct(ACC_NO_NEW) from AC_SEQN_MAP where ACC_NO_OLD = '"+accBindBan.getAcc_no()+"'),");
            sql.append("(select ACC_ID from MDM_ACC_REL where CERT_NO='"+accBindBan.getBnd_cert_no()+"' and OPN_BR_NO='850088'),");
            sql.append("1,");
            sql.append("(select ACC_NAME from MDM_ACC_REL where CERT_NO='"+accBindBan.getBnd_cert_no()+"' and OPN_BR_NO='850088'),");
            sql.append("'AT00',");
            sql.append("'"+accBindBan.getBnd_acc_no()+"',");
            sql.append("(select ACC_NAME from MDM_ACC_REL where CERT_NO='"+accBindBan.getBnd_cert_no()+"' and OPN_BR_NO='850088'),");
            sql.append("'20180401',");
            sql.append("'ZJ01',");
            sql.append("'"+accBindBan.getBnd_cert_no()+"',");
            sql.append("'"+accBindBan.getBnd_phone_no()+"',");
            sql.append("'BF00',");
            sql.append("'AF00',");
            sql.append("'BS00',");
            sql.append("'850088'");
            sql.append(");");
            sql.append("\n");
        }
        sql.append("select * from "+table+";");

        //insert into ACC_BIN_REL (ACC_NO,ACC_ID,ACC_SEQN,ACC_NAME,BND_ACC_TYPE,BND_ACC_NO,BND_ACC_NAME,BND_DATE,BND_CERT_TYPE,BND_CERT_NO,BND_PHONE_NO,BND_BANK_FLAG,BND_AUTH_FLAG,BND_STS,BR_NO)
        // values((select distinct(ACC_NO_NEW) from AC_SEQN_MAP where ACC_NO_OLD = ''),null,1,'name','AT00','bnd_acc_no','name','20180401','ZJ01','bnd_cert_no','bnd_phone_no','BF00','AF00','BS00','850088');

    }
    public static List<AccBindBan> csv2List(List<CsvRow> rows){
        List<AccBindBan> accBindBans = new ArrayList<>();
        for (CsvRow row : rows){
            AccBindBan accBindBan = new AccBindBan();
            Object[] objects = row.toArray();
            String acc_no = objects[0].toString().trim();
            String bnd_acc_no = objects[1].toString().trim();
            String bnd_phone_no = objects[2].toString().trim();
            String bnd_cert_no = objects[3].toString().trim();
            accBindBan.setAcc_no(acc_no);
            accBindBan.setBnd_acc_no(bnd_acc_no);
            accBindBan.setBnd_cert_no(bnd_cert_no);
            accBindBan.setBnd_phone_no(bnd_phone_no);
            accBindBans.add(accBindBan);
        }
        return accBindBans;
    }
}
