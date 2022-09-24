package com.coding.xt.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.xt.pojo.Subject;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:43
 */

public interface SubjectMapper extends BaseMapper<Subject> {

    /**
     * 按课程ID查找主题列表
     * @param courseId
     * @return
     */
    List<Subject> findSubjectListByCourseId(Long courseId);

}
