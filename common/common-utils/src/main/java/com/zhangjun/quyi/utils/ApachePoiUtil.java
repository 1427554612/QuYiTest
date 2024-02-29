package com.zhangjun.quyi.utils;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class ApachePoiUtil {

    /**
     * 基于POI向Excel文件写入数据
     * @throws Exception
     */
    public static void write() throws Exception{
        //在内存中创建一个Excel文件对象
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建Sheet页
        XSSFSheet sheet = excel.createSheet();
        for (int i = 0; i < 50000; i++) {
            XSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(UUID.randomUUID().toString().replaceAll("-","").substring(0,24));
        }


        FileOutputStream out = new FileOutputStream(new File("D:\\user-id.xlsx"));
        excel.write(out);

        //关闭资源
        out.flush();
        out.close();
        excel.close();
    }
    public static void main(String[] args) throws Exception {
        write();
    }

}
