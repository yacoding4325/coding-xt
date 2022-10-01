package com.coding.xt.web.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author yaCoding
 * @create 2022-10-01 下午 9:15
 */

//用户优惠券模型
@Data
public class UserCouponModel {

    private String name;

    private BigDecimal amount;

    private Long couponId;

}
