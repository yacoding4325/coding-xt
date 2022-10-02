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
    //当做一个小作业 订单支付完成之后，应该发一个消息到队列，队列接收到之后 订单完成了，把课程列表的缓存 更新一下
//    @Cache(name = "web_courseList",time = 5*60*1000,hasUser = true)
    public CallResult courseList(@RequestBody CourseParam courseParam){
        return courseService.courseList(courseParam);
    }

    /**
     * 主题信息
     * @param courseParam
     * @return
     */
    @RequestMapping("subjectInfo")
    public CallResult subjectInfo(@RequestBody CourseParam courseParam){
        return courseService.subjectInfo(courseParam);
    }

    @PostMapping(value = "courseDetail")
    public CallResult courseDetail(@RequestBody CourseParam courseParam){
        return courseService.courseDetail(courseParam);
    }

    @PostMapping(value = "myCoupon")
    public CallResult myCoupon(@RequestBody CourseParam courseParam){
        return courseService.myCoupon(courseParam);
    }

}
