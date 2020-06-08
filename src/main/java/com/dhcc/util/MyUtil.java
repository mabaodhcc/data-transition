package com.dhcc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: dreamdongx
 * @create: 2020-05-21 17:06
 * @version: 1.0.0
 */
public class MyUtil {
    private MyUtil(){}
    public static void autoCreateEntity(String className,String fieldStr,String split){
        String[] fields = getFields(fieldStr,split);
        StringBuilder classStr = new StringBuilder();
        getClassStr(classStr,className,fields);
        createClass(classStr.toString(),className);
    }

    private static void getClassStr(StringBuilder classStr, String className, String[] fields) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        String dateStr = sdf.format(date);
        classStr.append("package " + ConfigEntity.PACKAGE + ";\n");
        classStr.append("/**\n *@author: dreamdongx\n *@create: "+dateStr+"\n *@version: 1.0.0\n */\n");
        classStr.append("public class " + className + "{");
        for(String field : fields){
            classStr.append("\t");
            classStr.append("private String " + field + ";");
            classStr.append("\n");
        }
        //    public String getAcc_no() {
        //        return acc_no;
        //    }
        //
        //    public void setAcc_no(String acc_no) {
        //        this.acc_no = acc_no;
        //    }
        for(String field : fields){
            classStr.append("\tpublic String get" +field.substring(0,1).toUpperCase() + field.substring(1) + "() {\n");
            classStr.append("\t\treturn "+field+";\n");
            classStr.append("\t}\n");
            classStr.append("\tpublic void set" +field.substring(0,1).toUpperCase() + field.substring(1) + "(String "+field+") {\n");
            classStr.append("\t\tthis." + field + "=" + field +";\n");
            classStr.append("\t}\n");
        }
        classStr.append("}");

    }

    private static void createClass(String classStr,String className) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(ConfigEntity.PATH + File.separator + className + ".java");
            fileWriter.write(classStr);
            //fileWriter.write("Hello World!");
            fileWriter.flush();
            System.out.println(className);
            System.out.println("类创建成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileWriter != null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String[] getFields(String fieldStr, String split) {
        String[] fields = fieldStr.split(split);
        return fields;
    }


}
