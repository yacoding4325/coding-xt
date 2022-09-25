package com.coding.xt.web.api;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.CourseParam;
import com.coding.xt.web.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:11
 */
@RestController
@RequestMapping("course")
public class CourseApi {

    @Autowired
    private CourseService courseService;

    @PostMapping(value = "courseList")
    public CallResult courseList(@RequestBody CourseParam courseParam){
        return courseService.courseList(courseParam);
    }

}
