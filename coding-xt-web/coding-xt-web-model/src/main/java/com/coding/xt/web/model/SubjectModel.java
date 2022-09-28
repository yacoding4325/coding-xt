package com.coding.xt.web.model;

import lombok.Data;

import java.util.List;

/**
 * @author Jarno
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
