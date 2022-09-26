package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coding.web.domain.UserCourseDomain;
import com.coding.xt.pojo.UserCourse;
import com.coding.xt.web.dao.UserCourseMapper;
import com.coding.xt.web.model.params.UserCourseParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:21
 */
@Component
public class UserCourseDomainRepository {

    @Resource
    private UserCourseMapper userCourseMapper;

    public UserCourseDomain createDomain(UserCourseParam userCourseParam) {
        return new UserCourseDomain(this,userCourseParam);
    }

    public UserCourse findUserCourse(Long userId, Long courseId, long currentTime) {
        LambdaQueryWrapper<UserCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCourse::getCourseId,courseId);
        queryWrapper.eq(UserCourse::getUserId,userId);
        queryWrapper.ge(UserCourse::getExpireTime,currentTime);
        return userCourseMapper.selectOne(queryWrapper);
    }

    public long countUserCourseByCourseId(Long courseId) {
        LambdaQueryWrapper<UserCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCourse::getCourseId,courseId);
        return userCourseMapper.selectCount(queryWrapper);
    }

    public Integer countUserCourseByUserId(List<Long> courseIdList, long currentTimeMillis) {
        LambdaQueryWrapper<UserCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserCourse::getCourseId,courseIdList);
        queryWrapper.ge(UserCourse::getExpireTime,currentTimeMillis);
        return userCourseMapper.selectCount(queryWrapper);
    }

    public Integer countUserCourse(Long courseId) {
        return null;
    }

    public UserCourse findUserCourseByUserIdAndCourseId(Long courseId, Long userId, Long time) {
        return null;
    }
}
