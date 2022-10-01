package com.coding.xt.web.service;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.CourseParam;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:14
 */

public interface CourseService {
    /**
     * 课程列表
     * @param courseParam
     * @return
     */
    CallResult courseList(CourseParam courseParam);

    /**
     * 根据课程id查询详情信息
     * @param courseParam
     * @return
     */
    CallResult subjectInfo(CourseParam courseParam);

    /**
     * 课程详情就是根据课程id，将课程的信息展示出来，供用户查看
     * @param courseParam
     * @return
     */
    CallResult courseDetail(CourseParam courseParam);

}
