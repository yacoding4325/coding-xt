package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.web.domain.*;
import com.coding.web.domain.mq.MqService;
import com.coding.xt.common.wx.config.WxPayConfiguration;
import com.coding.xt.pojo.Order;
import com.coding.xt.pojo.OrderTrade;
import com.coding.xt.web.dao.OrderMapper;
import com.coding.xt.web.dao.OrderTradeMapper;
import com.coding.xt.web.model.params.*;
import com.sun.xml.internal.bind.v2.schemagen.episode.Klass;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.security.krb5.internal.tools.Kinit;

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

    @Autowired
    public WxPayConfiguration wxPayConfiguration;

    @Resource
    private OrderTradeMapper orderTradeMapper;

    @Autowired
    private UserCourseDomainRepository userCourseDomainRepository;


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

    //更新订单的支付id
    public void updatePayOrderId(Order order) {
        LambdaUpdateWrapper<Order> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Order::getId,order.getId());
        updateWrapper.set(Order::getPayOrderId,order.getPayOrderId());
        this.orderMapper.update(null, updateWrapper);
    }

    //更新订单状态和付款状态
    public void updateOrderStatusAndPayStatus(Order order) {
        LambdaUpdateWrapper<Order> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Order::getId,order.getId());
        updateWrapper.set(Order::getOrderStatus,order.getOrderStatus());
        updateWrapper.set(Order::getPayStatus,order.getPayStatus());
        updateWrapper.set(Order::getPayTime,order.getPayTime());
        this.orderMapper.update(null, updateWrapper);
    }



    //查找订单交易
    public OrderTrade findOrderTrade(String orderId) {
        LambdaQueryWrapper<OrderTrade> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderTrade::getOrderId,orderId);
        queryWrapper.last("limit 1");
        return orderTradeMapper.selectOne(queryWrapper);
    }

    //存储订单交易
    public void saveOrderTrade(OrderTrade orderTrade) {
        orderTradeMapper.insert(orderTrade);
    }

    //更新订单交易
    public void updateOrderTrade(OrderTrade orderTrade) {
        LambdaUpdateWrapper<OrderTrade> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(OrderTrade::getId,orderTrade.getId());
        updateWrapper.set(OrderTrade::getPayInfo,orderTrade.getPayInfo());
        this.orderTradeMapper.update(null, updateWrapper);
    }

    //按付款订单ID查找订单
    public Order findOrderByPayOrderId(String orderId) {
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getPayOrderId,orderId);
        return this.orderMapper.selectOne(queryWrapper);
    }

    //创建资源域
    public UserCourseDomain createUserCourseDomain(UserCourseParam userCourseParam) {
        return userCourseDomainRepository.createDomain(userCourseParam);
    }

    //订单列表
    public Page<Order> orderList(Long userId, int orderStatus, int currentPage, int pageSize) {
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getUserId,userId);
        queryWrapper.ne(Order::getOrderStatus,orderStatus);
        Page<Order> page = new Page<>(currentPage,pageSize);
        return this.orderMapper.selectPage(page,queryWrapper);
    }
}
