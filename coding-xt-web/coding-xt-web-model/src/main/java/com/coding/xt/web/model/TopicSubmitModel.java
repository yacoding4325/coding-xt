package com.coding.xt.web.model;

import lombok.Data;

@Data
public class TopicSubmitModel {

    private boolean answerFlag;

    private Integer topicType;

    private String answer;

    private boolean finish;


}
