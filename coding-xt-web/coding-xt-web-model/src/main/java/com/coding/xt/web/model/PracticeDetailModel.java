package com.coding.xt.web.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PracticeDetailModel {
    private TopicModelView topic;
    private Integer total;
    private String practiceId;
    private String createTime;
    private String finishTime;
    private String useTime;
    private String subjectName;
    private List<Integer> subjectUnitList;
    private Integer answered;
    private Integer trueNum;
    private Integer wrongNum;
    private Integer noAnswer;
    private Integer progress;
    private List<Map<String,Object>> topicAnswerStatusList;

    public Integer getNoAnswer(){
        if (total != null & answered != null){
            return total - answered;
        }
        return total;
    }
}
