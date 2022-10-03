package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import com.sun.xml.internal.bind.v2.schemagen.episode.Klass;
import org.aspectj.weaver.ast.Or;
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

    //按订单ID查找订单
    public Order findOrderByOrderId(String orderId) {
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getPayOrderId,orderId);
        return this.orderMapper.selectOne(queryWrapper);
    }

    //更新订单状态
    public void updateOrderStatus(int initCode, Order order) {
        LambdaUpdateWrapper<Order> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Order::getId,order.getId());
        updateWrapper.eq(Order::getOrderStatus,initCode);
        updateWrapper.set(Order::getOrderStatus,order.getOrderStatus());
        this.orderMapper.update(null, updateWrapper);
    }

}
