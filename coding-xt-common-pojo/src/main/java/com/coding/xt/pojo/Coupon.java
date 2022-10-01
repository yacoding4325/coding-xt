package com.coding.xt.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author yaCoding
 * @create 2022-10-01 下午 9:11
 */
//塑造个人的优惠劵
//息票
@Data
public class Coupon {

    private Long id;
    private String name;
    private String couponDesc;
    private BigDecimal price;

    //满多少才能使用优惠劵
    private BigDecimal max;
    private Integer number;
    private Integer useNumber;
    private Integer status;

    //开始时间
    private Long startTime;

    //过期时间
    private Long expireTime;

    //是否需要满减 0不需要 1 需要
    private Integer disStatus;

}
