package com.zhangjun.quyi.api_auto_test.util;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import java.io.File;
import java.util.List;

public class EasyExcelUtil{

    /**
     * 读取excel数据、封装指定对象
     * @param t
     * @param filePath
     * @param <T>
     * @return
     */
    public static <T>List<T> readExcel(T t, String filePath){
        List<T> entitys = ExcelImportUtil.importExcel(new File(filePath), t.getClass(), new ImportParams());
        return entitys;
    }

}
