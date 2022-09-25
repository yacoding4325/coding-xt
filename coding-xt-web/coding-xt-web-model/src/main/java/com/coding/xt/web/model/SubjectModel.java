package com.coding.xt.web.model;

import lombok.Data;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-25 上午 11:08
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
