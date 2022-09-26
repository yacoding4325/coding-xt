package com.coding.xt.web.model.params;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:01
 */
//用户课程参数
@Data
public class UserCourseParam {

//    public Long getCourseId() {
//        return null;
//    }
//
//    public Long getUserId() {
//        return null;
//    }
//
//    public Long getTime() {
//        return null;
//    }

    private Long time;
    private Long userId;
    private Long id;
    private String courseName;
    private String courseDesc;
    private String subjects;
    private BigDecimal coursePrice;
    private BigDecimal courseZhePrice;
    private Integer courseStatus;
    private List<Integer> subjectIdList;
    private Integer orderTime;//订购时长
    private int page = 1;
    private int pageSize = 20;
    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;

    private Long courseId;
    private String imageUrl;
}
