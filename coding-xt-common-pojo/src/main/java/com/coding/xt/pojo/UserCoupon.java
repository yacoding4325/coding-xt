package com.coding.xt.pojo;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-10-01 下午 9:13
 */
@Data
public class UserCoupon {

    private Long id;

    private Long userId;

    private Long couponId;

    // 0 未使用 1 已使用 2过期
    private Integer status;

    private Long startTime;

    private Long expireTime;

    //使用时间
    private Long useTime;

    //来源
    private String source;

}
