package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.AccMangeRel;
import com.dhcc.entity.EcifTemp;
import com.dhcc.entity.ProductRel;
import com.dhcc.util.MyCsvUtil;
import com.dhcc.util.TempDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class 客户经理关联05 {
    public static void main(String[] args) {
        String fileName = "绩效.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        List<AccMangeRel> accMangeRels = toAccMangeRel(rows);
        StringBuilder stringBuilder = new StringBuilder();
        List<EcifTemp> ecifTemps = new ArrayList<>( TempDateUtil.getEcifTemps().values());
        List<ProductRel> productRels = new ArrayList<>( TempDateUtil.getProductRels().values());

        for (AccMangeRel accMangeRel: accMangeRels) {
//            String  开户机构编码=accMangeRel.get开户机构编码();
            String  开户机构编码= Constant.br_no;
            String  旧的客户号=accMangeRel.get客户号();
            String  旧的产品编号=accMangeRel.get产品编号();
            Optional<EcifTemp> ecifTempOptional = ecifTemps.stream().filter(p->p.get旧的客户号().equals(旧的客户号)).findFirst();
            Optional<ProductRel> productRelOptional = productRels.stream().filter(p->p.get旧的产品编号().equals(旧的产品编号)).findFirst();

            if (!ecifTempOptional.isPresent()){
                continue;
            }
            if (!productRelOptional.isPresent()){
                continue;
            }
            ProductRel  productRel=productRelOptional.get();
            String  新的产品编号=productRel.get现在产品编号();
            EcifTemp ecifTemp=ecifTempOptional.get();
            String  新的客户号=ecifTemp.get新的客户号();
            String  旧的电子账户=accMangeRel.get电子账号();
            String  新的电子账号=ecifTemp.get新的电子账户();

            String  新的账户序号=ecifTemp.get新的账户序号();
//            提供的csv数据缺少ACC_ID这个列
            String acc_id=ecifTemp.get介质账户ACC_ID();
            String  是否关联客户经理标志=accMangeRel.get是否关联客户经理标志();
            String  客户经理客户号= accMangeRel.get客户经理客户号();
            String  客户经理编号=accMangeRel.get客户经理编号();
            String  客户经理名=accMangeRel.get客户经理名();
            //绩效 ACC_MANGE_REL账户客户经理关联表
            String acc_mange_rel="INSERT INTO ACC_MANGE_REL (\"OPN_BR_NO\", \"CIF_NO\", \"ACC_NO\", \"ACC_ID\", \"ACC_SEQN\", \"CIF_MANGE_REL\", \"MANGE_CIF\", \"MANGE_NO\", \"MANGE_NAME\", \"PRDT_NO\") " +
                    " VALUES ('"+开户机构编码+"', '"+新的客户号+"', '"+新的电子账号+"', '"+acc_id+"', '"+新的账户序号+"', '"+是否关联客户经理标志+"', "+客户经理客户号+", '"+客户经理编号+"', '"+客户经理名+"', '"+新的产品编号+"');\n";
            stringBuilder.append(acc_mange_rel);
        }
        MyCsvUtil.writFile(stringBuilder.toString(),fileName);
    }
    public static List<AccMangeRel> toAccMangeRel(List<CsvRow> rows){
        List list=new ArrayList<AccMangeRel>(rows.size());
        for (CsvRow row: rows) {
            Object [] object=row.toArray();
            //AccMangeRel accMangeRel =new AccMangeRel(object[0].toString().trim(),object[1].toString().trim(),object[2].toString().trim(),object[3].toString().trim(),object[4].toString().trim(),object[5].toString().trim(),object[6].toString().trim(),object[7].toString().trim(),object[8].toString().trim());
            //list.add(accMangeRel);
        }
        return list;
    }
}
