package com.coding.xt.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:55
 */
//七牛云图片地址配置，枚举，分页模型等
@Data
public class ListPageModel<T> implements Serializable {

    private long pageCount;

    private int page;

    private int pageSize;

    private long size;

    private List<T> list;


    public ListPageModel<T> initNull(){
        ListPageModel<T> listModel = new ListPageModel<T>();
        listModel.setList(new ArrayList<T>());
        listModel.setPage(1);
        listModel.setPageCount(1);
        listModel.setSize(0);
        return listModel;
    }

}

