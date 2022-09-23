package com.coding.xt.admin.params;

import lombok.Data;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-22 下午 5:59
 */
//主题参数
@Data
public class TopicParam {
    private Long id;
    private Integer topicType;
    private String topicTitle;
    private String topicImg;
    private String answer;
    private String topicAnalyze;
    private Long topicSubject;
    private Integer topicStar;
    private String topicAreaPro;
    private String topicAreaCity;
    private Integer page = 1;
    private Integer pageSize = 20;

    //subject
    private Long subjectId;

    private Long userId;
    //题目ID
    private Long topicId;
    //单元
    private Integer subjectUnit;
    //单元
    private List<Integer> subjectUnitList;

    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;

    //批量导入需求
    private String addAdmin;

    private String topicChoice;

    private String topicAnswer;

    private Long createTime;

    private Long lastUpdateTime;
}
