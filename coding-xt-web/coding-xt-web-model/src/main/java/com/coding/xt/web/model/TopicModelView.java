package com.coding.xt.web.model;

import com.coding.xt.common.model.topic.ContentAndImage;
import com.coding.xt.common.model.topic.FillBlankChoice;
import lombok.Data;

import java.util.List;
import java.util.Map;
//主题模型视图
@Data
public class TopicModelView {

    private Long id;

    private String topicTitle;

    private Integer topicType;

    private List<String> topicImgList;

    private Integer topicStar;

    private String topicAreaPro;

    private String topicAreaCity;

    private List<FillBlankChoice> fillBlankAnswer;

    private int fillBlankTopicChoice;

    private List<Map<String, ContentAndImage>> radioChoice;

    private List<Map<String,ContentAndImage>> mulChoice;

    private String answer;

    private String analyze;

    private Integer subject;

    private String lastUpdateTime;

    private String userAnswer;

    private Integer pStatus;
}