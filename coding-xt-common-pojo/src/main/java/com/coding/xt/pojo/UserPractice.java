package com.coding.xt.pojo;

import lombok.Data;
//用户实践
@Data
public class UserPractice {
    private Long id;
    private Long historyId;
    private Long topicId;
    private Long userId;
    private Integer pStatus;
    private String userAnswer;
}