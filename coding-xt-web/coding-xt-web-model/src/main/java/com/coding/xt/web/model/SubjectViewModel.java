package com.coding.xt.web.model;

import lombok.Data;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 2:48
 */
@Data
public class SubjectViewModel {

    private Long id;

    private String subjectName;

    private String subjectGrade;

    private String subjectTerm;


    private List<Integer> SubjectUnitList;

    //所选列表
    private List<Integer> SubjectUnitSelectedList;

    public void fillSubjectName() {
        this.subjectName = this.subjectName + "-" +subjectGrade + "-" + subjectTerm;
    }

}
