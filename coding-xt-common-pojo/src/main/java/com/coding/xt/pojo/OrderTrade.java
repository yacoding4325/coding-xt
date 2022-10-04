package com.coding.xt.pojo;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-10-04 下午 7:24
 */
//订单交易
@Data
public class OrderTrade {

    private Long id;

    //订单Id
    private String orderId;

    //事务ID
    private String transactionId;

    //支付类型
    private Integer payType;

    private Long userId;

    //支付信息
    private String payInfo;

}
