package com.coding.xt.admin.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-24 下午 2:15
 */
//课程的类型
@Data
public class CourseModel {

    private String id;

    private String courseName;

    private String subjects;

    private BigDecimal coursePrice;
    private BigDecimal courseZhePrice;
    private Integer courseStatus;//正常，下架
    private Integer orderTime;
    private List<Long> subjectIdList;
    private List<SubjectModel> subjectList;
    private String imageUrl;

}
