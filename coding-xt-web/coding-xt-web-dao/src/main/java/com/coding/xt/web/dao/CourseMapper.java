package com.coding.xt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.pojo.Course;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:21
 */

public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据年级 进行课程的列表查询
     *
     * @param page
     * @param subjectGrade
     * @return
     */
    Page<Course> findCourseByGrade(Page<Course> page, String subjectGrade);
}
