package com.coding.xt.pojo;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:02
 */
//数据库的一些实体类
@Data
public class Subject {

    private Long id;
    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;
    private Integer status;

}
