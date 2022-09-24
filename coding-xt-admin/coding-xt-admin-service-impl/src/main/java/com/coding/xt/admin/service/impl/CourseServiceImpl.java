package com.coding.xt.admin.service.impl;

import com.coding.xt.admin.domain.CourseDomain;
import com.coding.xt.admin.domain.repository.CourseDomainRepository;
import com.coding.xt.admin.params.CourseParam;
import com.coding.xt.admin.service.CourseService;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yaCoding
 * @create 2022-09-23 下午 10:18
 */
@Service
public class CourseServiceImpl extends AbstractService implements CourseService {

    @Autowired
    private CourseDomainRepository courseDomainRepository;


    //保存课程名称
    @Override
    public CallResult saveCourse(CourseParam courseParam) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParam);
        
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.saveCourse();
            }
        });
    }

    //更新课程
    @Override
    public CallResult updateCourse(CourseParam courseParam) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParam);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.updateCourse();
            }
        });
    }

    //查找课程内容
    @Override
    public CallResult findCourseById(CourseParam courseParam) {
        CourseDomain courseDomain = this.courseDomainRepository.createDomain(courseParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.findCourseById();
            }
        });
    }

    //查找页数
    @Override
    public CallResult findPage(CourseParam courseParam) {
        CourseDomain courseDomain = courseDomainRepository.createDomain(courseParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.findPage();
            }
        });
    }

}
