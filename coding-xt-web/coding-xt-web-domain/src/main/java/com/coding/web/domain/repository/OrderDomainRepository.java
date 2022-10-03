package com.coding.web.domain.repository;

import com.coding.web.domain.CouponDomain;
import com.coding.web.domain.CourseDomain;
import com.coding.web.domain.OrderDomain;
import com.coding.web.domain.SubjectDomain;
import com.coding.web.domain.mq.MqService;
import com.coding.xt.pojo.Order;
import com.coding.xt.web.dao.OrderMapper;
import com.coding.xt.web.model.params.CouponParam;
import com.coding.xt.web.model.params.CourseParam;
import com.coding.xt.web.model.params.OrderParam;
import com.coding.xt.web.model.params.SubjectParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:49
 */
@Component
public class OrderDomainRepository {

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private CourseDomainRepository courseDomainRepository;

    @Autowired
    private CouponDomainRepository couponDomainRepository;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Autowired
    public MqService mqService;


    public OrderDomain createDomain(OrderParam orderParam) {
        return new OrderDomain(this,orderParam);
    }


    //创建课程域
    public CourseDomain createCourseDomain(CourseParam courseParam) {
        return courseDomainRepository.createDomain(courseParam);
    }

    //创建息票域
    public CouponDomain createCouponDomain(CouponParam couponParam) {
        return couponDomainRepository.createDomain(couponParam);
    }


    //存储顺序
    public void saveOrder(Order order) {
        this.orderMapper.insert(order);
    }


    //创建主题域
    public SubjectDomain createSubjectDomain(SubjectParam subjectParam) {
        return subjectDomainRepository.createDomain(subjectParam);
    }


}
