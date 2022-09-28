package com.coding.xt.pojo;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-09-28 上午 11:04
 */
@Data
public class UserProblem {

    private Long id;

    private Long userId;

    private Long subjectId;

    private Long topicId;

    private Integer errorCount;

    private Integer errorStatus;

    private String errorAnswer;

    private Long errorTime;

}
