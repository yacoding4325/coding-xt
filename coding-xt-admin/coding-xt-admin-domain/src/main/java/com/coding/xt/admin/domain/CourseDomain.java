package com.coding.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.admin.domain.repository.CourseDomainRepository;
import com.coding.xt.admin.model.CourseModel;
import com.coding.xt.admin.params.CourseParam;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.model.ListPageModel;
import com.coding.xt.pojo.Course;
import com.coding.xt.pojo.Subject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-09-23 下午 10:19
 */
//课程域
public class CourseDomain {

    private CourseParam courseParam;

    private CourseDomainRepository courseDomainRepository;

    public CourseDomain(CourseParam courseParam, CourseDomainRepository courseDomainRepository) {
        this.courseParam = courseParam;
        this.courseDomainRepository = courseDomainRepository;
    }

    private CourseModel copy(Course course) {
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(course,courseModel);
        courseModel.setId(courseModel.getId().toString());
        return courseModel;
    }

    private List<CourseModel> copyList(List<Course> courseList) {
        if (courseList == null || courseList.size() == 0) {
            return new ArrayList<>();
        }
        List<Subject> subjectList = this.courseDomainRepository.createSubjectDomain(null).findAllSubjectList();
        List<CourseModel> courseModelList = new ArrayList<>();
        for (Course course: courseList) {
            CourseModel model = copy(course);
            model.setSubjects(displaySubjects(subjectList,course.getSubjects()));
            courseModelList.add(model);
        }
        return courseModelList;
    }

    private String displaySubjects(List<Subject> subjectList,String subjects) {
        String[] ss = StringUtils.split(subjects,",");
        String ds = "";
        Map<String,String> map = new HashMap<>();
        for (Subject subject:subjectList) {
            map.put(String.valueOf(subject.getId()),subject.getSubjectName());
        }
        for(String s: ss) {
            if (StringUtils.isEmpty(ds)) {
                ds = map.get(s);
            } else  {
                ds += ","+ map.get(s);
            }
        }
        return ds;
    }

    private String displayCourseStatus(Integer courseStatus) {
        if (0 == courseStatus){
            return "正常";
        }
        if (1 == courseStatus){
            return "下架";
        }
        return "无此状态";
    }


    public CallResult<Object> saveCourse() {
        String courseName = this.courseParam.getCourseName();
        if (courseName == null){
            courseName = "";
        }
        String courseDesc = this.courseParam.getCourseDesc();
        if (courseDesc == null){
            courseDesc = "";
        }
        List<Long> subjectIdList = this.courseParam.getSubjectIdList();
        String subjects = "";
        for (Long subjectId: subjectIdList){
            subjects += subjectId + ",";
        }
        if (!StringUtils.isEmpty(subjects)){
            subjects = subjects.substring(0,subjects.length() - 1);
        }
        if (StringUtils.isEmpty(subjects)){
            return CallResult.fail(-999,"创建课程必须有学科");
        }
        List<String> subjectList = this.courseDomainRepository.createSubjectDomain(null).allSubjectIdList();

        String[] subjectsArr = StringUtils.split(subjects,",");
        for (String subjectId : subjectsArr){
            if (!subjectList.contains(subjectId)){
                return CallResult.fail(-999,"学科不存在");
            }
        }
        String imageUrl = this.courseParam.getImageUrl();
        if (StringUtils.isEmpty(imageUrl)){
            imageUrl = "";
        }
        BigDecimal cPrice = this.courseParam.getCoursePrice();
        BigDecimal cZhePrice = this.courseParam.getCourseZhePrice();
        Integer courseStatus = this.courseParam.getCourseStatus();
        Integer orderTime = this.courseParam.getOrderTime();

        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseDesc(courseDesc);
        course.setSubjects(subjects);
        course.setCoursePrice(cPrice);
        course.setCourseZhePrice(cZhePrice);
        course.setCourseStatus(courseStatus);
        course.setOrderTime(orderTime);
        course.setImageUrl(imageUrl);

        this.courseDomainRepository.saveCourse(course);
        for (Long subjectId: subjectIdList){
            this.courseDomainRepository.saveCourseSubject(subjectId,course.getId());
        }
        return CallResult.success();
    }

    public CallResult<Object> updateCourse() {
        Long id = this.courseParam.getId();
        Course course = this.courseDomainRepository.findCourse(id);
        if (course == null){
            return CallResult.fail(-999,"课程不存在");
        }
        String courseName = this.courseParam.getCourseName();
        if (courseName == null){
            courseName = "";
        }
        String courseDesc = this.courseParam.getCourseDesc();
        if (courseDesc == null){
            courseDesc = "";
        }
        List<Long> subjectIdList = this.courseParam.getSubjectIdList();
        String subjects = "";
        for (Long subjectId: subjectIdList){
            subjects += subjectId + ",";
        }
        if (!StringUtils.isEmpty(subjects)){
            subjects = subjects.substring(0,subjects.length() - 1);
        }
        if (this.courseParam.getOrderTime() != null){
            course.setOrderTime(this.courseParam.getOrderTime());
        }
        String imageUrl = this.courseParam.getImageUrl();
        if (!StringUtils.isEmpty(imageUrl)){
            course.setImageUrl(imageUrl);
        }
        BigDecimal cPrice = this.courseParam.getCoursePrice();
        BigDecimal cZhePrice = this.courseParam.getCourseZhePrice();
        Integer courseStatus = this.courseParam.getCourseStatus();

        course.setCourseName(courseName);
        course.setCourseDesc(courseDesc);
        course.setSubjects(subjects);
        course.setCoursePrice(cPrice);
        course.setCourseZhePrice(cZhePrice);
        course.setCourseStatus(courseStatus);
        course.setOrderTime(this.courseParam.getOrderTime());
        this.courseDomainRepository.updateCurse(course);
        this.courseDomainRepository.deleteCourseSubject(course.getId());
        for (Long subjectId: subjectIdList){
            this.courseDomainRepository.saveCourseSubject(subjectId,course.getId());
        }
        return CallResult.success();
    }

    public CallResult<Object> findPage() {
        int page = this.courseParam.getPage();
        int pageSize = this.courseParam.getPageSize();
        Page<Course> coursePage = this.courseDomainRepository.findCourseListPage(page,pageSize);

        ListPageModel listModel = new ListPageModel();
        listModel.setSize(coursePage.getTotal());
        List<Course> result = coursePage.getRecords();
        List<CourseModel> courseModelList = new ArrayList<>();
        for (Course course : result){
            CourseModel courseModel = copy(course);
            courseModelList.add(courseModel);
        }
        listModel.setList(courseModelList);
        return CallResult.success(listModel);
    }

    public CallResult<Object> findCourseById() {
        Long id = courseParam.getId();
        Course course = this.courseDomainRepository.findCourse(id);
        CourseModel courseModel = copy(course);
        List<Subject> subjectList = this.courseDomainRepository.createSubjectDomain(null).findSubjectListByCourseId(course.getId());
        List<Long> subjectIdList = new ArrayList<>();
        for (Subject subject : subjectList){
            subjectIdList.add(subject.getId());
        }
        courseModel.setSubjectIdList(subjectIdList);
        courseModel.setSubjectList(this.courseDomainRepository.createSubjectDomain(null).findAllSubjectModelList());
        return CallResult.success(courseModel);
    }
}
