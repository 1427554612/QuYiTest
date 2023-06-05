package com.zhangjun.quyi.utils;

import com.zhangjun.quyi.constans.EncodeConstant;

import java.io.*;

public class HtmlParseUtil {

    /**
     * 将css文件内嵌进html文件中
     * @param rootFile
     */
    public static File updateHtmlFile(String rootFile) throws IOException {
        // 写入html文件
        File srcHtmlFile = new File(rootFile +"/report.html");
        File newHtmlFile = new File(rootFile +"/report-"+String.valueOf(DateTimeUtil.getTransactionSecondId())+".html");
        BufferedReader srcHtmBr = new BufferedReader(new InputStreamReader(new FileInputStream(srcHtmlFile), EncodeConstant.ENCODE_UTF_8));
        BufferedWriter newHtmBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newHtmlFile,true),EncodeConstant.ENCODE_UTF_8));
        String srcHtmStr = "";
        while ((srcHtmStr = srcHtmBr.readLine())!=null){
            if (srcHtmStr.contains("<link href=\"assets/style.css\"")){
                continue;
            }
            newHtmBw.write(srcHtmStr+"\n");
        }

        // 读取css文件
        File cssFile = new File(rootFile + "/assets/"+"style.css");
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(cssFile), EncodeConstant.ENCODE_UTF_8));
        String cssStr = "";
        newHtmBw.write("<style type=\"text/css\">"+"\n");
        while ((cssStr = bf.readLine())!=null){
            newHtmBw.write(cssStr+"\n");
        }
        newHtmBw.write("</style>"+"\n");
        bf.close();
        srcHtmBr.close();
        newHtmBw.close();
        return newHtmlFile;
    }

}
