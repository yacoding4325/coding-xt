package com.coding.xt.admin.controller;

import com.coding.xt.admin.params.CourseParam;
import com.coding.xt.admin.service.CourseService;
import com.coding.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yaCoding
 * @create 2022-09-23 下午 10:14
 */
//课程控制管理器
@RestController
@RequestMapping("course")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    //保存课程名称
    @RequestMapping(value = "saveCourse")
    public CallResult saveCourse(@RequestBody CourseParam courseParam) {
        return courseService.saveCourse(courseParam);
    }

    //更新课程
    @RequestMapping(value = "updateCourse")
    public CallResult updateCourse(@RequestBody CourseParam courseParam){
        return courseService.updateCourse(courseParam);
    }

    //产找课程内容
    @RequestMapping(value = "findCourseById")
    public CallResult findSubjectById(@RequestBody CourseParam courseParam){
        return courseService.findCourseById(courseParam);
    }

    //查找页数
    @RequestMapping(value = "findPage")
    public CallResult findPage(@RequestBody CourseParam courseParam) {
        return courseService.findPage(courseParam);
    }

}
