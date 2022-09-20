package com.coding.xt.pojo;

/**
 * @Author yaCoding
 * @create 2022-09-20 上午 10:05
 */

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 新闻
 */
@Data
public class News {
    private Long id;
    private String title;
    private String summary;
    private String imageUrl;
    private String content;
    /**
     * 1 题库 2 升学 3 其他
     */
    private Integer tab;
    private Long createTime;
    private String author;
    /**
     * 0 正常 1删除
     */
    @TableField("n_status")
    private Integer status;
}
