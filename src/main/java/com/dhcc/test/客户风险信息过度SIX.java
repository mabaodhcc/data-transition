package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.AccMangeRel;
import com.dhcc.entity.客户风险信息过度表;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-25 09:54
 * @version: 1.0.0
 */
public class 客户风险信息过度SIX {
    public static void main(String[] args) {
        String fileName="客户风险信息过渡.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        List<客户风险信息过度表> list = csr2Entity(rows);
        //拼接sql的容器
        StringBuilder sql = new StringBuilder();
        getSql(sql,list);

        MyCsvUtil.writFile(sql.toString(),fileName);

    }

    private static void getSql(StringBuilder sql, List<客户风险信息过度表> list) {
        String table = "ecif_risk_info";
        for(客户风险信息过度表 l : list){
            sql.append("insert into " + table +" values(");
            sql.append("(select cif_no_new from cif_info_mid where cif_no_old = '"+l.getCif_no_old()+"'),");
            sql.append("(select cif_name from cif_info_mid where cif_no_old = '"+l.getCif_no_old()+"'),");
            sql.append("(select cert_no from cif_info_mid where cif_no_old = '"+l.getCif_no_old()+"'),");
            sql.append("'ZJ01',");
            sql.append("'"+l.getDate()+"',");
            sql.append("'99991231',");
            sql.append("'"+l.getRisk_rvl()+"',");
            if("RL00".equals(l.getRisk_rvl())){
                sql.append("'谨慎型',");
            }else if("RL01".equals(l.getRisk_rvl())){
                sql.append("'稳健型',");
            }else if("RL02".equals(l.getRisk_rvl())){
                sql.append("'平衡型',");
            }else if("RL03".equals(l.getRisk_rvl())){
                sql.append("'进取型',");
            }else if("RL04".equals(l.getRisk_rvl())){
                sql.append("'激进型',");
            }else{
                sql.append("null,");
            }
            sql.append("null,");
            sql.append("'"+ Constant.tx_br_no+"'");
            sql.append(");\n");
        }
        /*for(客户风险信息过度表 l : list){
            sql.append("'"+l.getCif_no_old()+"',");
        }*/
    }

    private static List<客户风险信息过度表> csr2Entity(List<CsvRow> rows) {
        List<客户风险信息过度表> list = new ArrayList<>();
        for (CsvRow row : rows){
            Object[] objects = row.toArray();
            客户风险信息过度表 temp = new 客户风险信息过度表();
            temp.setCif_no_old(objects[0].toString().trim());
            temp.setDate(objects[1].toString().trim());
            temp.setRisk_rvl(objects[2].toString().trim());
            list.add(temp);
        }
        return list;
    }
}
