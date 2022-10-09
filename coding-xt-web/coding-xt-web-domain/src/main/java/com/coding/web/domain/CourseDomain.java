package com.coding.web.domain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.web.domain.repository.CourseDomainRepository;
import com.coding.xt.common.login.UserThreadLocal;
import com.coding.xt.common.model.BusinessCodeEnum;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.model.ListPageModel;
import com.coding.xt.pojo.*;
import com.coding.xt.web.dao.CourseSubjectMapper;
import com.coding.xt.web.model.*;
import com.coding.xt.web.model.enums.HistoryStatus;
import com.coding.xt.web.model.params.CouponParam;
import com.coding.xt.web.model.params.CourseParam;
import com.coding.xt.web.model.params.SubjectParam;
import com.coding.xt.web.model.params.UserCourseParam;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 4:17
 */
//课程域
public class CourseDomain {

    private CourseDomainRepository courseDomainRepository;
    private CourseParam courseParam;

    public CourseDomain(CourseDomainRepository courseDomainRepository, CourseParam courseParam) {
        this.courseDomainRepository = courseDomainRepository;
        this.courseParam = courseParam;
    }

    public CallResult<Object> courseList() {
        /**
         * 1. 如果根据年级进行查询，需要先找到年级对应的科目列表，根据科目列表去查询课程列表
         * 2. 如果年级为空，查询全部的课程即可
         * 3. 用户购买课程的信息，课程中科目的名称信息
         * 4. 判断用户是否登录，如果登录 去user_course 表 去查询相关信息
         * 5. 根据课程id，去查询对应的科目名称
         */
        int page = this.courseParam.getPage();
        int pageSize = this.courseParam.getPageSize();
        String subjectGrade = this.courseParam.getSubjectGrade();
        Page<Course> coursePage;
        if (StringUtils.isNotBlank(subjectGrade)){
            coursePage = this.courseDomainRepository.findCourseByGrade(page,pageSize,subjectGrade);
        }else{
            coursePage = this.courseDomainRepository.findAllCourse(page,pageSize);
        }
        List<Course> courseList = coursePage.getRecords();
        List<CourseViewModel> courseViewModels = new ArrayList<>();
        for (Course course : courseList) {
            CourseViewModel courseViewModel = new CourseViewModel();
            BeanUtils.copyProperties(course,courseViewModel);
            //购买的数量
            long studyCount = this.courseDomainRepository.createUserCourseDomain(new UserCourseParam()).countUserCourseByCourseId(course.getId());
            courseViewModel.setStudyCount((int) studyCount);
            Long userId = UserThreadLocal.get();
            if (userId != null){
                //代表用户已登录
                UserCourse userCourse = this.courseDomainRepository.createUserCourseDomain(new UserCourseParam()).findUserCourse(userId,course.getId(),System.currentTimeMillis());
                if (userCourse == null){
                    courseViewModel.setBuy(0);
                }else {
                    courseViewModel.setBuy(1);
                    courseViewModel.setExpireTime(new DateTime(userCourse.getExpireTime()).toString("yyyy-MM-dd"));
                }
            }
            //科目信息，根据课程id查找对应的科目信息
            List<SubjectModel> subjectModelList = this.courseDomainRepository.createSubjectDomain(new SubjectParam()).findSubjectListByCourseId(course.getId());
            courseViewModel.setSubjectList(subjectModelList);
            courseViewModel.setSubjectInfo(createSubjectModel(subjectModelList));
            courseViewModels.add(courseViewModel);

        }
        ListPageModel<CourseViewModel> listPageModel = new ListPageModel<>();
        listPageModel.setSize(coursePage.getTotal());
        listPageModel.setPageCount(coursePage.getPages());
        listPageModel.setPage(page);
        listPageModel.setPageSize(pageSize);
        listPageModel.setList(courseViewModels);
        return CallResult.success(listPageModel);
    }

    private SubjectModel createSubjectModel(List<SubjectModel> subjectModelList) {
        SubjectModel subjectModel = new SubjectModel();
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder termBuilder = new StringBuilder();
        for (SubjectModel model : subjectModelList) {
            if (!nameBuilder.toString().contains(model.getSubjectName())) {
                nameBuilder.append(model.getSubjectName()).append(",");
            }
            if (!termBuilder.toString().contains(model.getSubjectTerm())) {
                termBuilder.append(model.getSubjectTerm()).append(",");
            }
        }
        String name = nameBuilder.toString();
        subjectModel.setSubjectName(name.substring(0,name.lastIndexOf(",")));
        subjectModel.setSubjectGrade(subjectModelList.get(0).getSubjectGrade());
        String term = termBuilder.toString();
        subjectModel.setSubjectTerm(term.substring(0,term.lastIndexOf(",")));
        return subjectModel;
    }

    public CallResult<Object> subjectInfo() {
        Long userId = UserThreadLocal.get();
        /**
         * 1. 根据课程id 查询 学科列表
         * 2. 根据学科 查询对应的单元
         * 3. 返回对应的模型数据即可
         * 4. 不做的业务逻辑（如果此课程的学科已经在学习中，返回已经当初选择的单元）
         */
        Long courseId = courseParam.getCourseId();
        List<SubjectModel> subjectModelList = this.courseDomainRepository.createSubjectDomain(null).findSubjectListByCourseId(courseId);

        List<SubjectViewModel> subjectViewModelList = new ArrayList<>();
        for (SubjectModel subjectModel : subjectModelList) {

            SubjectViewModel subjectViewModel = new SubjectViewModel();
            subjectViewModel.setId(subjectModel.getId());
            subjectViewModel.setSubjectGrade(subjectModel.getSubjectGrade());
            subjectViewModel.setSubjectTerm(subjectModel.getSubjectTerm());
            subjectViewModel.setSubjectName(subjectModel.getSubjectName());
            subjectViewModel.fillSubjectName();
            List<Integer> subjectUnitLis
                    = this.courseDomainRepository.createSubjectDomain(null).findSubjectUnitBySubjectId(subjectModel.getId());
            subjectViewModel.setSubjectUnitList(subjectUnitLis);

            if (userId != null){
                UserHistory userHistory = this.courseDomainRepository.createUserHistoryDomain(null).findUserHistory(userId, subjectModel.getId(), HistoryStatus.NO_FINISH.getCode());
                if (userHistory != null) {
                    String subjectUnits = userHistory.getSubjectUnits();
                    if (StringUtils.isNotEmpty(subjectUnits)) {
                        List<Integer> strings = JSON.parseArray(subjectUnits, Integer.class);
                        subjectViewModel.setSubjectUnitSelectedList(strings);
                    }
                }
            }

            subjectViewModelList.add(subjectViewModel);
        }
        return CallResult.success(subjectViewModelList);
    }

    public CallResult<Object> checkSubjectInfoParam() {
        Long courseId = courseParam.getCourseId();
        Course course = this.courseDomainRepository.findCourseById(courseId);
        if (course == null){
            return CallResult.fail(BusinessCodeEnum.TOPIC_PARAM_ERROR.getCode(),"参数错误");
        }
        return CallResult.success();
    }

    public List<Long> findCourseIdListBySubjectId(Long subjectId) {
        return courseDomainRepository.findCourseIdListBySubjectId(subjectId);
    }

    public CallResult<Object> courseDetail() {
        Long courseId = this.courseParam.getCourseId();
        Course course = this.courseDomainRepository.findCourseById(courseId);
        if (course == null){
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(),"课程不存在");
        }
        CourseDetailModel courseDetailModel = new CourseDetailModel();
        courseDetailModel.setCourseId(courseId);
        courseDetailModel.setCourseName(course.getCourseName());
        courseDetailModel.setCourseTime(course.getOrderTime());
        courseDetailModel.setPrice(course.getCourseZhePrice());
        //根据课程id 查询课程关联的科目详情
        List<SubjectModel> subjectModelList = this.courseDomainRepository.createSubjectDomain(null).findSubjectListByCourseId(courseId);
        StringBuilder subjectStr = new StringBuilder();
        for (SubjectModel subject : subjectModelList) {
            subjectStr.append(subject.getSubjectName())
                    .append(" ")
                    .append(subject.getSubjectGrade())
                    .append(" ")
                    .append(subject.getSubjectTerm())
                    .append(",");
        }
        String subjectInfo = subjectStr.toString().substring(0, subjectStr.toString().length() - 1);
        courseDetailModel.setSubjectInfo(subjectInfo);

        return CallResult.success(courseDetailModel);
    }

    public CallResult<Object> myCoupon() {
        /**
         * 1.根据课程和当前的登录用户id 查询用户所有可用的优惠劵
         * 2. 判断优惠券是否可用 开始时间 不等于-1 过期时间 不等于-1
         */
        Long userId = UserThreadLocal.get();
        Long courseId = this.courseParam.getCourseId();
        Course course = this.courseDomainRepository.findCourseById(courseId);
        if (course == null) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(),"课程不存在");
        }
        List<UserCoupon> userCouponList = this.courseDomainRepository.createUserCouponDomain(new CouponParam()).findUserCouponByUserId(userId);
        List<UserCouponModel> userCouponModelList = new ArrayList<>();
        for (UserCoupon userCoupon : userCouponList) {
            Long startTime = userCoupon.getStartTime();
            Long expireTime = userCoupon.getExpireTime();
            long currentTimeMillis = System.currentTimeMillis();
            if (startTime != -1 && startTime > currentTimeMillis) {
                //时间没到 不能用
                continue;
            }
            if (expireTime != -1 && expireTime < currentTimeMillis){
                //已过期不能用
                continue;
            }
            //判断满减
            Long couponId = userCoupon.getCouponId();
            Coupon coupon = this.courseDomainRepository.createUserCouponDomain(null).findCouponById(couponId);
            if (coupon == null) {
                continue;
            }
            Integer status = coupon.getStatus();
            if (2 == status) {
                continue;
            }
            Integer disStatus = coupon.getDisStatus();
            if (1 == disStatus){
                //需要满足满减条件
                BigDecimal max = coupon.getMax();
                BigDecimal courseZhePrice = course.getCourseZhePrice();
                if (max.compareTo(courseZhePrice) > 0){
                    continue;
                }
            }
            UserCouponModel userCouponModel = new UserCouponModel();
            userCouponModel.setCouponId(couponId);
            userCouponModel.setAmount(coupon.getPrice());
            userCouponModel.setName(coupon.getName());
            userCouponModelList.add(userCouponModel);
        }
        return CallResult.success(userCouponModelList);
    }

    public Course findCourseById(Long courseId) {
        return this.courseDomainRepository.findCourseById(courseId);
    }

    public CourseViewModel findCourseViewModel(Long courseId) {
        Course course = this.courseDomainRepository.findCourseById(courseId);
        return copyViewModel(course);
    }

    public CourseViewModel copyViewModel(Course course) {
        CourseViewModel courseViewModel = new CourseViewModel();
        courseViewModel.setId(course.getId());
        courseViewModel.setCourseDesc(course.getCourseDesc());
        courseViewModel.setCourseName(course.getCourseName());
        courseViewModel.setCoursePrice(course.getCoursePrice());
        courseViewModel.setCourseZhePrice(course.getCourseZhePrice());
        courseViewModel.setOrderTime(course.getOrderTime());
        courseViewModel.setImageUrl(course.getImageUrl());
        List<SubjectModel> subjectModelList = courseDomainRepository.createSubjectDomain(null).findSubjectListByCourseId(course.getId());
        courseViewModel.setSubjectList(subjectModelList);
        return courseViewModel;
    }

    public CallResult<Object> myCourse() {
        Long userId = UserThreadLocal.get();
        List<UserCourse> userCourseList = this.courseDomainRepository.createUserCourseDomain(null).findUserCourseList(userId);
        List<UserCourseModel> userCourseModels = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();
        for (UserCourse userCourse : userCourseList) {
            UserCourseModel userCourseModel = new UserCourseModel();
            Course course = this.courseDomainRepository.findCourseById(userCourse.getCourseId());
            userCourseModel.setCourseName(course.getCourseName());
            if (currentTime > userCourse.getExpireTime()){
                userCourseModel.setStatus(2);
            }else {
                userCourseModel.setStatus(1);
            }
            userCourseModel.setBuyTime(new DateTime(userCourse.getCreateTime()).toString("yyyy年MM月dd日"));
            userCourseModel.setExpireTime(new DateTime(userCourse.getExpireTime()).toString("yyyy年MM月dd日"));
            List<SubjectModel> subjectInfoByCourseId = this.courseDomainRepository.createSubjectDomain(new SubjectParam()).findSubjectListByCourseId(userCourse.getCourseId());
            Integer count = this.courseDomainRepository.createUserHistoryDomain(null).countUserHistoryBySubjectList(userId,subjectInfoByCourseId);
            userCourseModel.setStudyCount(count);
            userCourseModel.setCourseId(course.getId());
            userCourseModels.add(userCourseModel);
        }
        return CallResult.success(userCourseModels);
    }


//    public static void main(String[] args) {
//        System.out.println(new DateTime(1633511129910L).toString("yyyy-MM-dd"));
//        System.out.println(System.currentTimeMillis() + 300 * 24 * 60 * 60 * 1000);
//    }

}
