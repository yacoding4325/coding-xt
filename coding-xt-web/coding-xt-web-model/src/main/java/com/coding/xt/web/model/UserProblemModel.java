package com.coding.xt.web.model;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-10-09 下午 8:34
 */

@Data
public class UserProblemModel {

    private Long problemId;

    private TopicModelView topic;

    private SubjectModel subject;

    private Integer errorCount;

    private String errorAnswer;

    private String errorTime;

}
