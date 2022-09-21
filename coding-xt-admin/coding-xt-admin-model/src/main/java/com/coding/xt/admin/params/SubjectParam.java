package com.coding.xt.admin.params;

import lombok.Data;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:17
 */
//主题参数
@Data
public class SubjectParam {

    private Long id;
    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;
    private List<Integer> subjectUnits;

    private Integer status;

    private int page = 1;
    private int pageSize = 20;

    private String queryString;

    private int currentPage;

}
