package com.zhangjun.quyi.api_auto_test.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    /**
     *
     * @return
     */
    public static void  exportExcel(Class<?> pojoClass, Collection<?> dataSet, String path) throws IOException {
        File savefile = new File(path);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),pojoClass, dataSet);
        FileOutputStream fos = new FileOutputStream(path);
        workbook.write(fos);
        fos.close();
    }

}
