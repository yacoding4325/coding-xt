package com.coding.web.service.impl;

import com.coding.web.domain.CourseDomain;
import com.coding.web.domain.repository.CourseDomainRepository;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import com.coding.xt.web.model.params.CourseParam;
import com.coding.xt.web.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:16
 */
@Service
public class CourseServiceImpl extends AbstractService implements CourseService {

    @Autowired
    private CourseDomainRepository courseDomainRepository;

    @Override
    public CallResult courseList(CourseParam courseParam) {
        CourseDomain courseDomain = courseDomainRepository.createDomain(courseParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return courseDomain.courseList();
            }
        });
    }
}
