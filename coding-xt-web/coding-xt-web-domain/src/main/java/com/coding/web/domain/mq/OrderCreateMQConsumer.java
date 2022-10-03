package com.coding.web.domain.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.coding.web.domain.repository.OrderDomainRepository;
import com.coding.xt.pojo.Order;
import com.coding.xt.web.model.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 10:49
 */
@Component
@RocketMQMessageListener(topic = "create_order_delay",consumerGroup = "create_order_group")
@Slf4j
public class OrderCreateMQConsumer implements RocketMQListener<String>  {

    @Autowired
    private OrderDomainRepository orderDomainRepository;

    //消息发送过来 会转为json的字符串
    @Override
    public void onMessage(String message) {
        log.info("收到的消息:{},时间:{}",message,new DateTime().toString("HH:mm:ss"));
        /**
         * 1. 收到消息 订单id 还有 时间（用于判断订单是否能取消）
         * 2. 根据订单id进行查询订单
         */
        Map<String, String> messageMap = JSON.parseObject(message, new TypeReference<Map<String, String>>() {
        });
        String orderId = messageMap.get("orderId");
        //秒
        Integer time = Integer.parseInt(messageMap.get("time"));
        Order order = this.orderDomainRepository.findOrderByOrderId(orderId);
        if (order != null){
            if (order.getOrderStatus() != OrderStatus.INIT.getCode()
                    || order.getOrderStatus() != OrderStatus.COMMIT.getCode()){
                log.info("订单已经被其他程序所修改:{}",orderId);
                return;
            }
            Long createTime = order.getCreateTime();
            Long current = System.currentTimeMillis();
            if (current - createTime < time*1000){
                throw new RuntimeException("时间没到，不能取消订单");
            }
            order.setOrderStatus(OrderStatus.CANCEL.getCode());
            int initCode = OrderStatus.INIT.getCode();
            this.orderDomainRepository.updateOrderStatus(initCode,order);
            //优惠劵 也得还回去
            Long userId = order.getUserId();
            Long couponId = order.getCouponId();
            if (couponId > 0){
                this.orderDomainRepository.createCouponDomain(null).updateCouponNoUseStatus(userId,couponId,4);
            }
            log.info("订单长时间未支付，已经被取消:{}",orderId);
        }

    }
}
