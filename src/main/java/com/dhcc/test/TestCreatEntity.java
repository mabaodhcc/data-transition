package com.dhcc.test;

import com.dhcc.util.MyUtil;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-21 17:48
 * @version: 1.0.0
 */
public class TestCreatEntity {
    public static void main(String[] args) {

        String className = "AccMangeRel";
        String fieldStr = "开户机构编码,客户号,电子账号,账户序号,是否关联客户经理标志,客户经理客户号,客户经理编号,客户经理名,产品编号";
        String split = ",";
        MyUtil.autoCreateEntity(className,fieldStr,split);
    }

}
