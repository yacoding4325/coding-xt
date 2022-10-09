package com.coding.xt.web.model;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-10-09 下午 5:36
 */
@Data
public class UserCourseModel {

    private String courseName;
    /**
     * 1 有效
     * 2，已过期
     */
    private Integer status;

    private String buyTime;

    private String expireTime;

    private Integer studyCount;

    private Long courseId;

}
