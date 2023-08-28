package com.zhangjun.quyi.utils;

import com.zhangjun.quyi.constans.EncodeConstant;
import com.zhangjun.quyi.constans.HtmlConstant;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.resultVo.DataTree;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class FileUtil {

    /**
     * 生成数据树
     * @return
     */
    public static DataTree initFileTree(String rootPath) throws Exception {
        // 构建一级树结构
        File rootPathFile = new File(rootPath);
        if (!rootPathFile.exists()) throw new Exception(rootPath + "路径不存在");
        DataTree dataTree = new DataTree();
        // 设置一级数数据
        dataTree.setId(UUID.randomUUID().toString());
        dataTree.setLabel(rootPathFile.getName());
        dataTree.setFile(false);
        dataTree.setPath(rootPathFile.getPath());
        dataTree.setChildren(new ArrayList<>());
        crateChildrenTree(dataTree,rootPathFile);
        return dataTree;
    }

    /**
     * 递归生成子数据数
     * @param dataTree
     * @param rootPathFile
     */
    private static void crateChildrenTree(DataTree dataTree,File rootPathFile){
        for (File file : rootPathFile.listFiles()) {
            if (file.isFile()){
                DataTree children = new DataTree();
                children.setId(UUID.randomUUID().toString());
                children.setLabel(file.getName());
                children.setFile(true);
                children.setPath(file.getPath());
                children.setChildren(null);
                dataTree.getChildren().add(children);
            }else {
                DataTree children = new DataTree();
                children.setId(UUID.randomUUID().toString());
                children.setLabel(file.getName());
                children.setFile(false);
                children.setPath(file.getPath());
                children.setChildren(new ArrayList<>());
                dataTree.getChildren().add(children);
                crateChildrenTree(children,file);
            }
        }
    }

    /**
     * 根据查询条件读取log文件
     * @param queryMap
     * @return
     */
    public static String readLogByQueryMap(Map<String, Object> queryMap) throws Exception {
        File file = new File((String)queryMap.get("path"));
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), EncodeConstant.ENCODE_UTF_8));
        StringBuilder sb = new StringBuilder();
        String str = null;
        while ((str = bf.readLine()) != null){
            log.info("log日志文件的数据：" + str);
            if (str.contains("开始执行"))
                sb.append(HtmlConstant.DIV_PREFIX)
                .append("<span  style='color:blue'>")
                .append(str)
                .append(HtmlConstant.SPAN_SUFFIX)
                .append(HtmlConstant.DIV_SUFFIX)
                .append(HtmlConstant.BR_SUFFIX);
            else if (str.contains("执行完毕")){
                sb.append(HtmlConstant.DIV_PREFIX)
                        .append("<span  style='color:#33CC66'>")
                        .append(str)
                        .append(HtmlConstant.SPAN_SUFFIX)
                        .append(HtmlConstant.DIV_SUFFIX)
                        .append(HtmlConstant.BR_SUFFIX);
            }
            else if (str.contains("响应内容")){
                sb.append(HtmlConstant.DIV_PREFIX)
                        .append("<span  style='color:#FF9933'>")
                        .append(str)
                        .append(HtmlConstant.SPAN_SUFFIX)
                        .append(HtmlConstant.DIV_SUFFIX)
                        .append(HtmlConstant.BR_SUFFIX);
            }
            else if (str.contains("主流程报错")){
                sb.append(HtmlConstant.DIV_PREFIX)
                        .append("<span  style='color:#FF0033'>")
                        .append(str)
                        .append(HtmlConstant.SPAN_SUFFIX)
                        .append(HtmlConstant.DIV_SUFFIX)
                        .append(HtmlConstant.BR_SUFFIX);
            }
            else sb.append(HtmlConstant.DIV_PREFIX)
                    .append(str).append(HtmlConstant.DIV_SUFFIX).append(HtmlConstant.BR_SUFFIX);
        }
        bf.close();
        return sb.toString();
    }
}
