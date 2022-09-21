package com.coding.xt.admin.model;

import lombok.Data;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:17
 */

@Data
public class SubjectModel {
    private Long id;
    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;
    private Integer status;

    private List<Integer> subjectUnits;

    public void fillSubjectName() {
        this.subjectName = this.subjectName + "-" +subjectGrade + "-" + subjectTerm;
    }
}