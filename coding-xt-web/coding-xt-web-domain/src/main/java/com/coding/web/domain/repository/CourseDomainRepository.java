package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.web.domain.CourseDomain;
import com.coding.web.domain.SubjectDomain;
import com.coding.web.domain.UserCourseDomain;
import com.coding.xt.pojo.Course;
import com.coding.xt.web.dao.CourseMapper;
import com.coding.xt.web.model.params.CourseParam;
import com.coding.xt.web.model.params.SubjectParam;
import com.coding.xt.web.model.params.UserCourseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:17
 */
//课程域存储库
@Component
public class CourseDomainRepository {

    @Resource
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseDomainRepository userCourseDomainRepository;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

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

    //按照id查找课程
    public Course findCourseById(Long courseId) {
        return courseMapper.selectById(courseId);
    }
}
