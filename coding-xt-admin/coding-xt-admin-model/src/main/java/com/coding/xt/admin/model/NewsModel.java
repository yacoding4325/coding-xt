package com.coding.xt.admin.model;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-09-21 上午 9:29
 */
//新闻模型
@Data
public class NewsModel {
    private Long id;
    private String title;
    private String summary;
    private String imageUrl;
    private String content;
    private Integer tab;
    private String createTime;
    private String author;
}
