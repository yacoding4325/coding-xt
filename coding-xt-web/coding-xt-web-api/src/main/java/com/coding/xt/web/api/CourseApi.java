package com.coding.xt.web.api;

import com.coding.xt.common.annontation.NoAuth;
import com.coding.xt.common.cache.Cache;
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

    /**
     * **这里有个问题，由于课程列表中的内容涉及到当前的登录用户，所以需要将之前的缓存，
     * 添加一个参数，是否需要登录用户做为key**
     * @param courseParam
     * @return
     */
    @PostMapping(value = "courseList")
    @NoAuth
    @Cache(name = "web_courseList",time = 5*60*1000,hasUser = true)
    public CallResult courseList(@RequestBody CourseParam courseParam){
        return courseService.courseList(courseParam);
    }

}
