package com.coding.xt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.xt.pojo.Subject;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 3:50
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    //select * from t_subject where id in (SELECT subject_id FROM `t_course_subject` where course_id=9)
    List<Subject> findSubjectListByCourseId(Long courseId);
}
