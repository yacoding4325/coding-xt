package com.coding.xt.web.model.params;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:38
 */
//请求接口参数
@Data
public class OrderParam {

    private int page = 1;

    private int pageSize = 20;

    private Long userId;

    private Long courseId;

    private Long couponId;

    private Integer payType;

    private String orderId;

    private Integer orderStatus;

    private HttpServletRequest request;

    //昵称
    private String nickname;

}
