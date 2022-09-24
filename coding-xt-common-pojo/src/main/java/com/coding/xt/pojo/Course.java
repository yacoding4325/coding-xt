package com.coding.xt.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:08
 */
//课程
@Data
public class Course {

    private Long id;
    private String courseName;
    private String courseDesc;
    private String subjects;
    private BigDecimal coursePrice;
    private BigDecimal courseZhePrice;
    private Integer orderTime;//天数
    private Integer courseStatus;//正常，下架
    private String imageUrl;

}
