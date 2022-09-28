package com.coding.xt.web.model;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-09-27 下午 11:00
 */
//提交模型
@Data
public class TopicSubmitModel {

    private boolean answerFlag;

    private Integer topicType;

    private String answer;

    private boolean finish;

}
