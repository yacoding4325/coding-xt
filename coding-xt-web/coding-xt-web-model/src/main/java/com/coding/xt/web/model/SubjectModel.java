package com.coding.xt.web.model;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-25 上午 11:08
 */

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

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectGrade() {
        return subjectGrade;
    }

    public String getSubjectTerm() {
        return subjectTerm;
    }
}
