package com.coding.xt.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:27
 */

@Data
public class Order {

    private Long id;

    private Long userId;

    private String orderId;

    //付款订单Id
    private String payOrderId;

    //课程ID
    private Long courseId;

    //命令
    private BigDecimal orderAmount;

    //订单状态
    private Integer orderStatus;

    //支付类型
    private Integer payType;

    //支付状态
    private Integer payStatus;

    //创建时间
    private Long createTime;

    //到期时间
    private Integer expireTime;

    private Long payTime;

    //息票Id
    private Long couponId;

}
