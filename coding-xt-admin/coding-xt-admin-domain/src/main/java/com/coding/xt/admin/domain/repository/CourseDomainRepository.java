package com.coding.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.admin.dao.CourseMapper;
import com.coding.xt.admin.dao.CourseSubjectMapper;
import com.coding.xt.admin.domain.CourseDomain;
import com.coding.xt.admin.domain.SubjectDomain;
import com.coding.xt.admin.params.CourseParam;
import com.coding.xt.admin.params.SubjectParam;
import com.coding.xt.pojo.Course;
import com.coding.xt.pojo.CourseSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-09-23 下午 10:19
 */
@Component
public class CourseDomainRepository {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CourseSubjectMapper courseSubjectMapper;
    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    public void saveCourse(Course course) {
        courseMapper.insert(course);
    }

    public CourseDomain createDomain(CourseParam courseParam) {
        return new CourseDomain(courseParam,this);
    }
    public Course findCourse(Long id) {
        return courseMapper.selectById(id);
    }

    public void updateCurse(Course course) {
        this.courseMapper.updateById(course);
    }

    public SubjectDomain createSubjectDomain(SubjectParam subjectParam) {
        return subjectDomainRepository.createDomain(subjectParam);
    }

    public Page<Course> findCourseListPage(int currentPage, int pageSize) {
        Page<Course> page = new Page<>(currentPage,pageSize);
        return this.courseMapper.selectPage(page,new LambdaQueryWrapper<>());
    }

    public void saveCourseSubject(Long subjectId, Long courseId) {
        CourseSubject courseSubject = new CourseSubject();
        courseSubject.setCourseId(courseId);
        courseSubject.setSubjectId(subjectId);
        this.courseSubjectMapper.insert(courseSubject);
    }

    public void deleteCourseSubject(Long courseId) {
        LambdaQueryWrapper<CourseSubject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseSubject::getCourseId,courseId);
        this.courseSubjectMapper.delete(queryWrapper);
    }

}
