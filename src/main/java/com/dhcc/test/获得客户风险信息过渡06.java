package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.constant.Constant;
import com.dhcc.entity.ECIF_RISK_INFO;
import com.dhcc.entity.Ecif;
import com.dhcc.entity.EcifTemp;
import com.dhcc.util.MyCsvUtil;
import com.dhcc.util.TempDateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class 获得客户风险信息过渡06 {
    public static void main(String[] args) {
        String fileName="客户风险信息过渡.csv";
        List<CsvRow> ecifFXRows = MyCsvUtil.getData(fileName);
        ecifFXRows.remove(0);
        List<ECIF_RISK_INFO> temp=toECIF_RISK_INFO(ecifFXRows);
       //过滤掉不在过渡电子账户部分客户信息.csv文件中的数据
//        List<ECIF_RISK_INFO> ecif_risk_infos=filter(temp);
        List<EcifTemp> ecifTemps = new ArrayList<>(TempDateUtil.getEcifTemps().values());

        StringBuilder stringBuilder=new StringBuilder();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar =Calendar.getInstance();
        for (ECIF_RISK_INFO row: temp) {
            String 旧的客户号,评估日期,评估等级,新的客户号;
            旧的客户号=row.get客户号().trim();
            评估日期=row.get评估日期().trim();
            评估等级=row.get评估等级().trim();
            Optional<EcifTemp> ecifTempOptional = ecifTemps.stream().filter(p->p.get旧的客户号().equals(旧的客户号)).findFirst();
            if (!ecifTempOptional.isPresent()){
                continue;
            }
            EcifTemp ecifTemp=ecifTempOptional.get();
            新的客户号=ecifTemp.get新的客户号();
            String 客户姓名=ecifTemp.get客户姓名();
//            String 客户名称 = row.get客户名称().trim();
            String 证件号=ecifTemp.get证件号();
//            String 证件号 = row.get证件号().trim();
            String 证件类型=ecifTemp.get证件类型();
//            String 证件类型=row.get证件类型().trim();
            String 等级描述 = null;
            int 截止日期间隔=184;
            try {
                calendar.setTime(df.parse(评估日期));
            }catch (Exception e){
                    new Throwable(e.getMessage());
            }
            calendar.add(Calendar.DATE,截止日期间隔);
            String 评估截止日期 =df.format(calendar.getTime());
            if (评估等级.equals("RL00")){
                等级描述="谨慎型";
            }
            else if(评估等级.equals("RL01")){
                等级描述="稳健型";
            }
            else if(评估等级.equals("RL02")){
                等级描述="平衡型";
            }
            else if(评估等级.equals("RL03")){
                等级描述="进取型";
            }
            else if(评估等级.equals("RL04")){
                等级描述="激进型";
            }
            String 机构号= Constant.br_no;
            String ECIF_RISK_INFO="INSERT INTO ECIF_RISK_INFO(\"CIF_NO\", \"CIF_NAME\", \"CERT_NO\", \"CERT_TYPE\", \"RISK_DATE\", \"RISK_END_DATE\",\"RISK_LVL\", \"RISK_MO\", \"RISK_DESC\", \"BR_NO\")" +
                        " VALUES ('"+新的客户号+"', '"+客户姓名+"', '"+证件号+"', '"+证件类型+"', '"+评估日期+"', '"+评估截止日期+"', '"+评估等级+"', '"+等级描述+"', NULL, '"+机构号+"');\n";
            stringBuilder.append(ECIF_RISK_INFO);

        }
        MyCsvUtil.writFile(stringBuilder.toString(),fileName);
    }

    public static List<ECIF_RISK_INFO> toECIF_RISK_INFO(List<CsvRow> csvRow ) {
        List<ECIF_RISK_INFO> ecif_risk_infos=new ArrayList<ECIF_RISK_INFO>(csvRow.size());
        for (CsvRow row: csvRow) {
            Object[] object = row.toArray();
            String 客户号,评估日期,评估等级;
            客户号=object[0].toString().trim();
            评估日期=object[1].toString().trim();
            评估等级=object[2].toString().trim();
            ecif_risk_infos.add(new ECIF_RISK_INFO(客户号,评估日期,评估等级));
        }
        return ecif_risk_infos;
    }

    public static List<ECIF_RISK_INFO>  filter(List<ECIF_RISK_INFO> ecif_risk_infos) {
        String ecifsfileName="过渡电子账户部分客户信息.csv";
        List<CsvRow> ecifsRows = MyCsvUtil.getData(ecifsfileName);
        ecifsRows.remove(0);
        List<Ecif> ecifs= 用户注册01.toEcifList(ecifsRows);
        Iterator<ECIF_RISK_INFO> iterator = ecif_risk_infos.iterator();
        Set<ECIF_RISK_INFO> datas=new HashSet<ECIF_RISK_INFO>(ecif_risk_infos.size());
        while (iterator.hasNext()) {
            ECIF_RISK_INFO item= iterator.next();
            for (Ecif ecif:
            ecifs) {
                if (ecif.get证件号().equals(item.get客户号())){
                    String 客户号=item.get客户号();
                    String 评估日期=item.get评估日期();
                    String 评估等级=item.get评估等级();
                    ECIF_RISK_INFO ecif_risk_info=new  ECIF_RISK_INFO(客户号,  评估日期,  评估等级) ;
                    ecif_risk_info.set客户名称(ecif.get客户姓名());
                    ecif_risk_info.set证件号(ecif.get证件号());
                    ecif_risk_info.set证件类型(ecif.get证件类型());
                    datas.add(ecif_risk_info);
                    break;
                }
            }
        }
        return new ArrayList<>(datas);
    }
}
