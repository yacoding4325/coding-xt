package com.coding.xt.web.model.params;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:24
 */
@Data
public class NewsParam {

    private int page = 1;
    private int pageSize = 20;
    private Long id;
    private Integer tab;

    private String title;
    private String summary;
    private String imageUrl;
    private String content;
    private Long createTime;
    private String author;
    /**
     * 0 正常 1 删除
     */
    private Integer status;
}
