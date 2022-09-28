package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.web.domain.CourseDomain;
import com.coding.web.domain.SubjectDomain;
import com.coding.web.domain.UserCourseDomain;
import com.coding.web.domain.UserHistoryDomain;
import com.coding.xt.pojo.Course;
import com.coding.xt.pojo.CourseSubject;
import com.coding.xt.web.dao.CourseMapper;
import com.coding.xt.web.dao.CourseSubjectMapper;
import com.coding.xt.web.model.params.CourseParam;
import com.coding.xt.web.model.params.SubjectParam;
import com.coding.xt.web.model.params.UserCourseParam;
import com.coding.xt.web.model.params.UserHistoryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:17
 */
//课程域存储库
@Component
public class CourseDomainRepository {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CourseSubjectMapper courseSubjectMapper;

    @Autowired
    private UserCourseDomainRepository userCourseDomainRepository;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Autowired
    private UserHistoryDomainRepository userHistoryDomainRepository;

    public CourseDomain createDomain(CourseParam courseParam){
        return new CourseDomain(this,courseParam);
    }

    public Page<Course> findCourseByGrade(int currentPage,
                                          int pageSize,
                                          String subjectGrade) {
        //select * from t_course where course_status = 0 and id in (select course_id from t_course_subject where subject_id in ( select id from t_subject where subject_grade='七年级') group by course_id)
        Page<Course> page = new Page<>(currentPage,pageSize);
        return courseMapper.findCourseByGrade(page,subjectGrade);
    }

    public Page<Course> findAllCourse(int currentPage,
                                      int pageSize) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getCourseStatus,0);
        Page<Course> page = new Page<>(currentPage,pageSize);
        Page<Course> courseIPage = courseMapper.selectPage(page, queryWrapper);
        return courseIPage;
    }

    public UserCourseDomain createUserCourseDomain(UserCourseParam userCourseParam) {
        return this.userCourseDomainRepository.createDomain(userCourseParam);
    }

    public SubjectDomain createSubjectDomain(SubjectParam subjectParam) {
        return this.subjectDomainRepository.createDomain(subjectParam);
    }

    public Course findCourseById(Long courseId) {
        return courseMapper.selectById(courseId);
    }

    public UserHistoryDomain createUserHistoryDomain(UserHistoryParam userHistoryParam) {
        return userHistoryDomainRepository.createDomain(userHistoryParam);
    }

    public List<Long> findCourseIdListBySubjectId(Long subjectId) {
        LambdaQueryWrapper<CourseSubject> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CourseSubject::getSubjectId,subjectId);
        List<CourseSubject> courseSubjects = this.courseSubjectMapper.selectList(queryWrapper);
        return courseSubjects.stream().map(CourseSubject::getCourseId).collect(Collectors.toList());
    }

}
