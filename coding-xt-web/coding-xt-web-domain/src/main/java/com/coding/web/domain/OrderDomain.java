package com.coding.web.domain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.web.domain.pay.WxPayDomain;
import com.coding.web.domain.repository.OrderDomainRepository;
import com.coding.xt.common.login.UserThreadLocal;
import com.coding.xt.common.model.BusinessCodeEnum;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.model.ListPageModel;
import com.coding.xt.common.utils.CommonUtils;
import com.coding.xt.pojo.*;
import com.coding.xt.web.model.CourseViewModel;
import com.coding.xt.web.model.OrderDisplayModel;
import com.coding.xt.web.model.OrderViewModel;
import com.coding.xt.web.model.SubjectModel;
import com.coding.xt.web.model.enums.OrderStatus;
import com.coding.xt.web.model.enums.PayStatus;
import com.coding.xt.web.model.enums.PayType;
import com.coding.xt.web.model.params.OrderParam;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:49
 */
@Slf4j
public class OrderDomain {

    private OrderDomainRepository orderDomainRepository;

    private OrderParam orderParam;

    public OrderDomain(OrderDomainRepository orderDomainRepository, OrderParam orderParam) {
        this.orderDomainRepository = orderDomainRepository;
        this.orderParam = orderParam;
    }

    public CallResult<Object> submitOrder() {
        /**
         * 1.拿到参数
         * 2.检查课程是否存在
         * 3.检查优惠劵是否可用（有没有传优惠劵，传了 判断优惠劵能不能用）
         * 4.生成订单 创建订单 进行保存
         * 5.返回用户订单的详情 涉及科目的信息展示
         */
        Long couponId = this.orderParam.getCouponId();
        Long courseId = this.orderParam.getCourseId();
        Long userId = UserThreadLocal.get();
        Course course = this.orderDomainRepository.createCourseDomain(null).findCourseById(courseId);
        if (course == null) {
            return CallResult.fail(BusinessCodeEnum.COURSE_NOT_EXIST.getCode(),"课程不存在");
        }
        BigDecimal couponPrice = BigDecimal.ZERO;
        if (couponId != null) {
            //查询优惠劵
            Coupon coupon = this.orderDomainRepository.createCouponDomain(null).findCouponById(couponId);
            couponPrice = checkCoupon(coupon,userId,course);
        }
        Long createTime = System.currentTimeMillis();
        String orderId = createTime + String.valueOf(CommonUtils.random5Num()) + userId%10000;
        Order order = new Order();
        order.setCourseId(courseId);
        order.setCouponId(couponId == null ?0:couponId);
        order.setCreateTime(createTime);
        order.setExpireTime(course.getOrderTime());
        //        BigDecimal subtract = couponPrice.subtract(couponPrice);
//        order.setOrderAmount(subtract.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtract);
        //为了测试  订单金额设置为1分钱
        order.setOrderAmount(BigDecimal.valueOf(0.01));
        order.setOrderId(orderId);
        order.setOrderStatus(OrderStatus.INIT.getCode());
        order.setPayType(PayType.WX.getCode());//默认微信支付
        order.setPayStatus(PayStatus.NO_PAY.getCode());
        order.setUserId(userId);
        order.setPayOrderId(orderId);
        order.setPayTime(0L);
        this.orderDomainRepository.saveOrder(order);
        List<SubjectModel> subjectList = this.orderDomainRepository.createSubjectDomain(null).findSubjectListByCourseId(courseId);
        OrderDisplayModel orderDisplayModel = new OrderDisplayModel();
        orderDisplayModel.setAmount(order.getOrderAmount());
        orderDisplayModel.setCourseName(course.getCourseName());
        orderDisplayModel.setOrderId(orderId);
        StringBuilder subject = new StringBuilder();
        for (SubjectModel subjectModel : subjectList) {
            subject.append(subjectModel.getSubjectName()).append(",");
        }
        if (subject.toString().length() > 0) {
            subject = new StringBuilder(subject.substring(0,subject.toString().length() - 1));
        }
        orderDisplayModel.setSubject(subject.toString());
        //订单创建成功了,发送延时消息
        //16代表30分钟 延迟30m执行消费 3代表10秒
        Map<String,String> map = new HashMap<>();
        map.put("orderId",order.getOrderId());
        //在消费方进行消费的时候，订单是否要取消 多长满足取消 以 time为准 s
        map.put("time","1800");
        this.orderDomainRepository.mqService.sendDelayMessage("create_order_delay",map,16);
        return CallResult.success(orderDisplayModel);
    }

    /**
     * 检测优惠劵是否合法 合法 返回对应的优惠金额
     * @param coupon
     * @return
     */
    private BigDecimal checkCoupon(Coupon coupon,Long userId,Course course) {
        CouponDomain couponDomain = this.orderDomainRepository.createCouponDomain(null);
        UserCoupon userCoupon = couponDomain.findUserCouponByUserId(userId,coupon.getId());
        if (userCoupon == null){
            //条件不符合
            return BigDecimal.ZERO;
        }
        Long startTime = userCoupon.getStartTime();
        Long expireTime = userCoupon.getExpireTime();
        long currentTimeMillis = System.currentTimeMillis();
        if (expireTime != -1 && currentTimeMillis > expireTime){
            //过期了
            return BigDecimal.ZERO;
        }
        if (startTime != -1 && currentTimeMillis < startTime){
            //未到使用时间
            return BigDecimal.ZERO;
        }
        if (coupon.getDisStatus() == 1){
            BigDecimal max = coupon.getMax();
            if (max.compareTo(course.getCourseZhePrice())>0){
                return BigDecimal.ZERO;
            }
        }
        //标记为 已使用 未消费  如果用户取消了订单 把优惠券 还回来
        userCoupon.setStatus(4);
        couponDomain.updateCouponStatus(userCoupon);
        return coupon.getPrice();
    }

    /**
     * /**
     * * 1. 获取到登录用户
     * * 2. 根据订单号 查询订单 检查订单状态和支付状态
     * * 3. 根据课程id 查询课程 确保课程的状态正常
     * * 4. 组装微信支付需要的参数，发起微信的调用，微信会给我们返回对应的二维码链接
     * * 5. 更改订单状态 为 已提交
     * @return
     */
    public CallResult<Object> wxPay() {
        Long userId = UserThreadLocal.get();
        String orderId = this.orderParam.getOrderId();
        Integer payType = this.orderParam.getPayType();

        Order order = this.orderDomainRepository.findOrderByOrderId(orderId);
        if (order == null) {
            return CallResult.fail(BusinessCodeEnum.ORDER_NOT_EXIST.getCode(), "订单不存在");
        }
        Integer orderStatus = order.getOrderStatus();
        if (orderStatus != OrderStatus.INIT.getCode()) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), "订单已被更改");
        }
        Integer payStatus = order.getPayStatus();
        if (payStatus != PayStatus.NO_PAY.getCode()) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), "订单已被支付");
        }
        Long courseId = order.getCourseId();
        Course course = this.orderDomainRepository.createCourseDomain(null).findCourseById(courseId);
        if (course == null) {
            return CallResult.fail(BusinessCodeEnum.COURSE_NOT_EXIST.getCode(), "课程状态不可用");
        }
        String payOrderId = System.currentTimeMillis() + String.valueOf(CommonUtils.random5Num()) + userId % 10000;
        order.setPayOrderId(payOrderId);
        order.setPayType(payType);
        //更新订单的支付id
        this.orderDomainRepository.updatePayOrderId(order);
        WxPayDomain wxPayDomain = new WxPayDomain(this.orderDomainRepository.wxPayConfiguration);
        WxPayService wxPayService = wxPayDomain.getWxPayService();
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setNotifyUrl(this.orderDomainRepository.wxPayConfiguration.wxNotifyUrl);
        orderRequest.setBody(course.getCourseName());
        orderRequest.setOutTradeNo(payOrderId);
        orderRequest.setProductId(String.valueOf(courseId));
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(String.valueOf(order.getOrderAmount().doubleValue())));//元转成分
        orderRequest.setSpbillCreateIp("182.92.102.161");
        orderRequest.setTradeType("NATIVE");
        orderRequest.setTimeStart(new DateTime(order.getCreateTime()).toString("yyyyMMddHHmmss"));

        try {
            WxPayNativeOrderResult orderResult = wxPayService.createOrder(orderRequest);

            String codeUrl = orderResult.getCodeUrl();
            //更新订单状态
            order.setOrderStatus(OrderStatus.COMMIT.getCode());
            this.orderDomainRepository.updateOrderStatus(OrderStatus.INIT.getCode(), order);
            return CallResult.success(codeUrl);
        } catch (WxPayException e) {
            e.printStackTrace();
            log.error("wxpay error:", e);
            return CallResult.fail(BusinessCodeEnum.PAY_ORDER_CREATE_FAIL.getCode(), "订单支付创建失败");
        }
    }

    public CallResult<Object> notifyOrder(String xmlData) {
        /**
         * 1. 解析微信参数 payOrderId
         * 2. 根据payOrderId进行订单查询
         * 3. 如果订单存在 订单更改为已支付
         * 4. 处理了交易的流水信息 做了一个保存
         * 5. 处理用户的课程，要给用户将购买的课程标识为已购买
         * 6. 优惠券 标识为已使用
         */
        WxPayDomain wxPayDomain = new WxPayDomain(this.orderDomainRepository.wxPayConfiguration);
        try {
            WxPayOrderNotifyResult notifyResult = wxPayDomain.getWxPayService().parseOrderNotifyResult(xmlData);
            String returnCode = notifyResult.getReturnCode();
            if ("SUCCESS".equals(returnCode)){
                //支付成功了
                //1. 交易id 传递到微信的支付订单号 payOrderId
                String payOrderId = notifyResult.getOutTradeNo();
                //微信方交易订单号
                String transactionId = notifyResult.getTransactionId();
                Order order = this.orderDomainRepository.findOrderByPayOrderId(payOrderId);
                if (order == null){
                    return CallResult.fail(BusinessCodeEnum.ORDER_NOT_EXIST.getCode(),"订单不存在");
                }
                if (order.getOrderStatus() == OrderStatus.PAYED.getCode()
                        && order.getPayStatus() == PayStatus.PAYED.getCode()){
                    //代表订单已经处理过了，无需进行重复处理
                    return CallResult.success();
                }
                order.setOrderStatus(OrderStatus.PAYED.getCode());
                order.setPayStatus(PayStatus.PAYED.getCode());
//              String timeEnd = notifyResult.getTimeEnd(); 支付完成时间
                order.setPayTime(System.currentTimeMillis());
                this.orderDomainRepository.updateOrderStatusAndPayStatus(order);
                //订单
                //添加支付信息
                OrderTrade orderTrade = this.orderDomainRepository.findOrderTrade(order.getOrderId());
                if (orderTrade == null){
                    //第一次添加 流水
                    orderTrade = new OrderTrade();
                    orderTrade.setPayInfo(JSON.toJSONString(notifyResult));
                    orderTrade.setOrderId(order.getOrderId());
                    orderTrade.setUserId(order.getUserId());
                    orderTrade.setPayType(order.getPayType());
                    orderTrade.setTransactionId(transactionId);
                    this.orderDomainRepository.saveOrderTrade(orderTrade);
                }else{
                    //以前添加过 更新即可 理论上 收到的流水应该是一致的
                    orderTrade.setPayInfo(JSON.toJSONString(notifyResult));
                    this.orderDomainRepository.updateOrderTrade(orderTrade);
                }
                //添加课程
                this.orderDomainRepository.createUserCourseDomain(null).saveUserCourse(order);
                Long couponId = order.getCouponId();
                if (couponId > 0) {
                    UserCoupon userCoupon = this.orderDomainRepository.createCouponDomain(null).findUserCouponByUserId(order.getUserId(), couponId);
                    if (userCoupon != null) {
                        userCoupon.setStatus(1);
                        this.orderDomainRepository.createCouponDomain(null).updateCouponStatus(userCoupon);
                    }
                }
                return CallResult.success();
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            return CallResult.fail(BusinessCodeEnum.PAY_ORDER_CREATE_FAIL.getCode(),"微信支付信息处理失败");
        }

        return CallResult.fail();
    }


    //查询订单
    public CallResult<Object> findOrder() {
        String orderId = this.orderParam.getOrderId();
        if (StringUtils.isEmpty(orderId)){
            return CallResult.fail(BusinessCodeEnum.ORDER_NOT_EXIST.getCode(),BusinessCodeEnum.ORDER_NOT_EXIST.getMsg());
        }
        Order order = this.orderDomainRepository.findOrderByOrderId(orderId);
        if (order == null){
            return CallResult.fail(BusinessCodeEnum.ORDER_NOT_EXIST.getCode(),BusinessCodeEnum.ORDER_NOT_EXIST.getMsg());
        }
        OrderViewModel orderViewModel = new OrderViewModel();
        orderViewModel.setOrderId(order.getOrderId());
        CourseViewModel courseViewModel = this.orderDomainRepository.createCourseDomain(null).findCourseViewModel(order.getCourseId());
        orderViewModel.setCourse(courseViewModel);
        orderViewModel.setOAmount(order.getOrderAmount());
        orderViewModel.setOrderStatus(order.getOrderStatus());
        orderViewModel.setPayStatus(order.getPayStatus());
        orderViewModel.setPayType(order.getPayType());
        orderViewModel.setCreateTime(new DateTime(order.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        orderViewModel.setExpireTime(new DateTime(order.getCreateTime() + order.getExpireTime()*24*60*60*1000).toString("yyyy-MM-dd HH:mm:ss"));
        Long couponId = order.getCouponId();
        if (couponId <= 0){
            orderViewModel.setCouponAmount(new BigDecimal(0));
        }else{
            Coupon coupon = this.orderDomainRepository.createCouponDomain(null).findCouponById(couponId);
            BigDecimal price = coupon.getPrice();
            orderViewModel.setCouponAmount(price);
        }
        return CallResult.success(orderViewModel);
    }

    public CallResult<Object> orderList() {
        int page = this.orderParam.getPage();
        int pageSize = this.orderParam.getPageSize();
        Long userId = UserThreadLocal.get();
        Page<Order> orderPage = this.orderDomainRepository.orderList(userId, OrderStatus.CANCEL.getCode(),page,pageSize);
        List<OrderViewModel> orderViewModelList = new ArrayList<>();
        for (Order order : orderPage.getRecords()){
            OrderViewModel orderViewModel = new OrderViewModel();
            orderViewModel.setOrderId(order.getOrderId());
            CourseViewModel courseViewModel = this.orderDomainRepository.createCourseDomain(null).findCourseViewModel(order.getCourseId());
            orderViewModel.setCourse(courseViewModel);
            orderViewModel.setOAmount(order.getOrderAmount());
            orderViewModel.setOrderStatus(order.getOrderStatus());
            orderViewModel.setPayStatus(order.getPayStatus());
            orderViewModel.setPayType(order.getPayType());
            orderViewModel.setCreateTime(new DateTime(order.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            orderViewModel.setExpireTime(new DateTime(order.getCreateTime() + order.getExpireTime()*24*60*60*1000).toString("yyyy-MM-dd HH:mm:ss"));
            Long couponId = order.getCouponId();
            if (couponId <= 0){
                orderViewModel.setCouponAmount(new BigDecimal(0));
            }else{
                Coupon coupon = this.orderDomainRepository.createCouponDomain(null).findCouponById(couponId);
                BigDecimal price = coupon.getPrice();
                orderViewModel.setCouponAmount(price);
            }
            orderViewModelList.add(orderViewModel);
        }
        ListPageModel listPageModel = new ListPageModel();
        int total = (int) orderPage.getTotal();
        listPageModel.setSize(total);
        listPageModel.setPageCount(orderPage.getPages());
        listPageModel.setPage(page);
        listPageModel.setList(orderViewModelList);
        return CallResult.success(listPageModel);
    }
}
