package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.AccBindBan;
import com.dhcc.entity.AccMangeRel;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-23 16:00
 * @version: 1.0.0
 */
public class 绩效 {
    public static void main(String[] args) {
        String fileName= "绩效.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        //拼接sql的容器
        StringBuilder sql = new StringBuilder();
        List<AccMangeRel> accMangeRels = csv2List(rows);
        getSql(sql,accMangeRels);
        MyCsvUtil.writFile(sql.toString(),fileName);

    }

    private static void getSql(StringBuilder sql, List<AccMangeRel> accMangeRels) {
        String table = "acc_mange_rel";
        for (AccMangeRel accMangeRel : accMangeRels){
            sql.append("insert into "+ table + " values(");
            sql.append("'"+ Constant.tx_br_no+"',");
            sql.append("(select cif_no_new from "+Constant.database_ecif+".cif_info_mid where cif_no_old = '"+accMangeRel.get客户号()+"'),");
            sql.append("(select acc_no_new from ac_seqn_map where acc_no_old = '"+accMangeRel.get电子账号()+"' and acc_seqn_old = '"+accMangeRel.get账户序号()+"'),");
            sql.append("(select acc_id from mdm_acc_rel where cert_no in (select bnd_cert_no from acc_bind_ban where bnd_phone_no = '"+accMangeRel.get客户经理客户号()+"') and acc_name='"+accMangeRel.get客户经理名()+"' and opn_br_no='"+Constant.tx_br_no+"'),");
            sql.append("(select acc_seqn_new from ac_seqn_map where acc_no_old = '"+accMangeRel.get电子账号()+"' and acc_seqn_old = '"+accMangeRel.get账户序号()+"'),");
            sql.append("'"+accMangeRel.get是否关联客户经理标志()+"',");
            sql.append("(select cif_no from mdm_acc_rel where cert_no in (select bnd_cert_no from acc_bind_ban where bnd_phone_no = '"+accMangeRel.get客户经理客户号()+"') and acc_name='"+accMangeRel.get客户经理名()+"' and opn_br_no='"+Constant.tx_br_no+"'),");
            sql.append("null,");
            sql.append("'"+accMangeRel.get客户经理名()+"',");
            sql.append("(select prdt_no from prdt_mapper where prdt_no_old='"+accMangeRel.get产品编号()+"')");
            sql.append(");");
            sql.append("\n");
        }
        sql.append("select count(1) from "+table);


    }

    private static List<AccMangeRel> csv2List(List<CsvRow> rows) {
        List<AccMangeRel> accMangeRels = new ArrayList<>();
        for (CsvRow row : rows){
            Object[] objects = row.toArray();
            AccMangeRel accMangeRel = new AccMangeRel();
            accMangeRel.set开户机构编码(objects[0].toString().trim());
            accMangeRel.set客户号(objects[1].toString().trim());
            accMangeRel.set电子账号(objects[2].toString().trim());
            accMangeRel.set账户序号(objects[3].toString().trim());
            accMangeRel.set是否关联客户经理标志(objects[4].toString().trim());
            accMangeRel.set客户经理客户号(objects[5].toString().trim());
            accMangeRel.set客户经理编号(objects[6].toString().trim());
            accMangeRel.set客户经理名(objects[7].toString().trim());
            accMangeRel.set产品编号(objects[8].toString().trim());
            accMangeRels.add(accMangeRel);
        }
        return accMangeRels;
    }
}
