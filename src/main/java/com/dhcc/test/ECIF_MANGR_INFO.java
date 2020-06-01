package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.entity.EcifMangrInfo;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-21 10:57
 * @version: 1.0.0
 */
public class ECIF_MANGR_INFO {
    public static void main(String[] args) {
        String fileName = "20200429-客户经理绩效数据bak.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        List<EcifMangrInfo> ecifMangrInfos = csv2EcifMangrInfo(rows);
        StringBuilder sql = new StringBuilder();
        getSql(sql,ecifMangrInfos);
        MyCsvUtil.writFile(sql.toString(),fileName);
    }

    private static void getSql(StringBuilder sql, List<EcifMangrInfo> ecifMangrInfos) {
        for(EcifMangrInfo ecifMangrInfo : ecifMangrInfos){
            System.out.println(ecifMangrInfo);
            sql.append(ecifMangrInfo);
            sql.append("\n");
        }
    }

    private static List<EcifMangrInfo> csv2EcifMangrInfo(List<CsvRow> rows) {
        List<EcifMangrInfo> ecifMangrInfos = new ArrayList<>();
        for(CsvRow row : rows){
            Object[] objects = row.toArray();
            EcifMangrInfo ecifMangrInfo = new EcifMangrInfo();
            ecifMangrInfo.setMangr_no(objects[0].toString().trim());
            ecifMangrInfo.setMangr_name(objects[1].toString().trim());
            ecifMangrInfo.setMangr_cert_no(objects[2].toString().trim());
            ecifMangrInfo.setMangr_cert_type("ZJ01");
            ecifMangrInfo.setCif_name(objects[3].toString().trim());
            ecifMangrInfo.setAcc_no(objects[4].toString().trim());
            ecifMangrInfo.setBan_acc_no(objects[5].toString().trim());
            ecifMangrInfos.add(ecifMangrInfo);
        }
        return ecifMangrInfos;
    }

}
