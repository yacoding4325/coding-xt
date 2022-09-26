package com.coding.web.domain;

import com.coding.web.domain.repository.UserCourseDomainRepository;
import com.coding.xt.pojo.UserCourse;
import com.coding.xt.web.model.params.UserCourseParam;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 5:09
 */

public class UserCourseDomain {

    private UserCourseDomainRepository userCourseDomainRepository;
    private UserCourseParam courseParam;

    public UserCourseDomain(UserCourseDomainRepository userCourseDomainRepository, UserCourseParam courseParam) {
        this.userCourseDomainRepository = userCourseDomainRepository;
        this.courseParam = courseParam;
    }

    public Integer countUserCourse(Long courseId) {
        return userCourseDomainRepository.countUserCourse(courseId);
    }

    public UserCourse findUserCourseByUserIdAndCourseId() {
        Long courseId = courseParam.getCourseId();
        Long userId = courseParam.getUserId();
        Long time = courseParam.getTime();
        return userCourseDomainRepository.findUserCourseByUserIdAndCourseId(courseId,userId,time);
    }



    public long countUserCourseByCourseId(Long id) {
        return 0;
    }

    public UserCourse findUserCourse(Long userId, Long id, long currentTimeMillis) {
        return null;
    }

    public Integer countUserCourseByUserId(Long userId, List<Long> courseIdList, long currentTimeMillis) {
        return null;
    }
}
