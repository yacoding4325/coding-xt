package com.coding.xt.admin.params;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-23 下午 10:16
 */
//课程参数
@Data
public class CourseParam {

    private Long userId;
    private Long id;
    private String courseName;
    private String courseDesc;
    private String subjects;
    private BigDecimal coursePrice;
    private BigDecimal courseZhePrice;
    private Integer courseStatus;
    private List<Long> subjectIdList;
    private Integer orderTime;//订购时长
    private int page = 1;
    private int pageSize = 20;
    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;
    private Long courseId;
    private String imageUrl;

}
