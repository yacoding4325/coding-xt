package com.coding.xt.pojo;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:10
 */
@Data
public class UserCourse {

    private Long id;
    private Long userId;
    private Long courseId;
    private Long createTime;
    private Long expireTime;
    private Integer studyCount;

}
