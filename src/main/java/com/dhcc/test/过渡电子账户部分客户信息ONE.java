package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.CifInfoEntity;
import com.dhcc.util.MyCsvUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-21 15:55
 * @version: 1.0.0
 * 生成中间表cif_info_mid
 */
public class 过渡电子账户部分客户信息ONE {
    public static void main(String[] args) {
        String fileName = "过渡电子账户部分客户信息.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
//        rows.remove(0);
        List<CifInfoEntity> cifInfoEntities = csv2CifInfoEntity(rows);
        StringBuilder sql = new StringBuilder();
        getSql(cifInfoEntities,sql);
        //sql 格式
        //insert into CIF_INFO_MID (CERT_NO,ACC_NO_NEW,ACC_ID,ACC_NO_OLD,CIF_NO_OLD,CERT_TYPE,CIF_NAME) values();
        MyCsvUtil.writFile(sql.toString(),fileName+"one");
    }

    private static void getSql(List<CifInfoEntity> cifInfoEntities, StringBuilder sql) {
        for(CifInfoEntity cifInfoEntity : cifInfoEntities){
            sql.append("insert into CIF_INFO_MID (CERT_NO,ACC_NO_NEW,ACC_ID,ACC_NO_OLD,CIF_NO_OLD,CERT_TYPE,CIF_NAME,CIF_NO_NEW) values(");
            sql.append("'"+cifInfoEntity.getCert_no()+"',");
            sql.append("null,");
            sql.append("null,");
            sql.append("'"+cifInfoEntity.getAcc_id()+"',");
            sql.append("'"+cifInfoEntity.getOld_cif_no()+"',");
            sql.append("'ZJ01',");
            sql.append("'"+cifInfoEntity.getCert_name()+"',");
            sql.append("(select CIF_NO from ECIF_CERT_INFO where CERT_NO='"+cifInfoEntity.getCert_no()+"' and BR_NO='"+ Constant.br_no+"')");
            sql.append(");");
            sql.append("\n");
        }
        sql.append("select * from CIF_INFO_MID");
    }

    private static List<CifInfoEntity> csv2CifInfoEntity(List<CsvRow> rows) {
        List<CifInfoEntity> cifInfoEntities = new ArrayList<>();
        for(CsvRow row : rows){
            Object[] objects = row.toArray();
            CifInfoEntity cifInfoEntity = new CifInfoEntity();
            cifInfoEntity.setOpen_br_no(objects[0].toString().trim());
            cifInfoEntity.setTrc_no(objects[1].toString().trim());
            cifInfoEntity.setCert_type(objects[2].toString().trim());
            cifInfoEntity.setCert_no(objects[3].toString().trim());
            cifInfoEntity.setCert_name(objects[4].toString().trim());
            cifInfoEntity.setPhone(objects[5].toString().trim());
            cifInfoEntity.setCert_begin(objects[6].toString().trim());
            cifInfoEntity.setCert_end(objects[7].toString().trim());
            cifInfoEntity.setCert_address(objects[8].toString().trim());
            cifInfoEntity.setReg_date(objects[9].toString().trim());
            cifInfoEntity.setReg_time(objects[10].toString().trim());
            cifInfoEntity.setAcc_id(objects[11].toString().trim());
            cifInfoEntity.setAcc_type(objects[12].toString().trim());
            cifInfoEntity.setAcc_state(objects[13].toString().trim());
            cifInfoEntity.setOpen_chnl(objects[14].toString().trim());
            cifInfoEntity.setOld_cif_no(objects[15].toString().trim());
            cifInfoEntity.setStmp(objects[16].toString().trim());
            cifInfoEntities.add(cifInfoEntity);

        }
        return cifInfoEntities;
    }
}
