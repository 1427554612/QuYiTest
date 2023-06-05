package com.zhangjun.quyi.resultVo;

import lombok.Data;

import java.util.List;


@Data
public class DataTree {
    private String id;
    private String label;
    private boolean isFile;
    private String path;
    private List<DataTree> children;
}