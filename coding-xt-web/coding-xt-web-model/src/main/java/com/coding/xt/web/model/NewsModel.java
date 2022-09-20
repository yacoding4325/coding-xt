package com.coding.xt.web.model;

/**
 * @Author yaCoding
 * @create 2022-09-20 上午 10:02
 */

import lombok.Data;

/**
 * 新闻
 */
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