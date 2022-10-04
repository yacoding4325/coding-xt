package com.coding.web.domain;

import com.coding.web.domain.pay.WxPayDomain;
import com.coding.web.domain.repository.OrderDomainRepository;
import com.coding.xt.common.login.UserThreadLocal;
import com.coding.xt.common.model.BusinessCodeEnum;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.utils.CommonUtils;
import com.coding.xt.pojo.Coupon;
import com.coding.xt.pojo.Course;
import com.coding.xt.pojo.Order;
import com.coding.xt.pojo.UserCoupon;
import com.coding.xt.web.model.OrderDisplayModel;
import com.coding.xt.web.model.SubjectModel;
import com.coding.xt.web.model.enums.OrderStatus;
import com.coding.xt.web.model.enums.PayStatus;
import com.coding.xt.web.model.enums.PayType;
import com.coding.xt.web.model.params.OrderParam;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.math.BigDecimal;
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

}
