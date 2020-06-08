package com.dhcc.test;

import cn.hutool.core.text.csv.CsvRow;
import com.dhcc.entity.PrdtRelMapper;
import com.dhcc.util.MyCsvUtil;

import java.util.ArrayList;
import java.util.List;

public class Prdtinforel {

    public static void main(String[] args) {


    }

    //获取旧的产品号和产品名称
    public static List<PrdtRelMapper> getPrdtMapperList(){
        String fileName= "产品对照关系表.csv";
        List<CsvRow> rows = MyCsvUtil.getData(fileName);
        rows.remove(0);
        List<PrdtRelMapper> prdtRelMappers = toPrdtMapperList(rows);
        return prdtRelMappers;
    }

    public static List<PrdtRelMapper> toPrdtMapperList(List<CsvRow> rows) {
        List<PrdtRelMapper> prdtRelMappers = new ArrayList<>(rows.size());
        for (CsvRow csvRow : rows) {
            Object[] object = csvRow.toArray();
            String old_prdt_no = object[0].toString().trim();
            String prdt_name = object[1].toString().trim();
            PrdtRelMapper mapper = new PrdtRelMapper(old_prdt_no,prdt_name);
            prdtRelMappers.add(mapper);
        }
        return prdtRelMappers;
    }
}
