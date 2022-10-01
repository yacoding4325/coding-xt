package com.coding.xt.web.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author yaCoding
 * @create 2022-10-01 下午 8:50
 */
//课程详细信息模型
@Data
public class CourseDetailModel {

    private Long courseId;
    private String courseName;
    private String subjectInfo;
    private Integer courseTime;
    private BigDecimal price;

}
