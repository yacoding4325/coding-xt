package com.coding.xt.admin.service;

import com.coding.xt.admin.params.CourseParam;
import com.coding.xt.common.model.CallResult;

/**
 * @Author yaCoding
 * @create 2022-09-23 下午 10:18
 */

public interface CourseService {
    /**
     * 保存课程名称
     * @param courseParam
     * @return
     */
    CallResult saveCourse(CourseParam courseParam);

    /**
     * 更新课程
     * @param courseParam
     * @return
     */
    CallResult updateCourse(CourseParam courseParam);

    /**
     * 查找课程内容
     * @param courseParam
     * @return
     */
    CallResult findCourseById(CourseParam courseParam);

    /**
     * 查找页数
     * @param courseParam
     * @return
     */
    CallResult findPage(CourseParam courseParam);
}
