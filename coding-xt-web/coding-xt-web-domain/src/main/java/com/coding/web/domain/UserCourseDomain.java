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
    private UserCourseParam userCourseParam;

    public UserCourseDomain(UserCourseDomainRepository userCourseDomainRepository, UserCourseParam userCourseParam) {
        this.userCourseDomainRepository = userCourseDomainRepository;
        this.userCourseParam = userCourseParam;
    }

    public UserCourse findUserCourse(Long userId, Long courseId,long currentTime) {
        return userCourseDomainRepository.findUserCourse(userId,courseId,currentTime);
    }

    public long countUserCourseByCourseId(Long courseId) {
        return userCourseDomainRepository.countUserCourseByCourseId(courseId);
    }

    public Integer countUserCourseInCourseIdList(Long userId, List<Long> courseIdList, long currentTimeMillis) {
        return userCourseDomainRepository.countUserCourseInCourseIdList(userId,courseIdList,currentTimeMillis);
    }
}
