package com.coding.xt.common.model;

import lombok.Data;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-10-07 下午 5:21
 */

@Data
public class ListModel<T> {

    private Integer total;

    private List<T> list;

}