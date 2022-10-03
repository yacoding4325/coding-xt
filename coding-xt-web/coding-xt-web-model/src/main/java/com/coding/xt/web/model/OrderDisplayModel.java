package com.coding.xt.web.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:40
 */
@Data
public class OrderDisplayModel {

    private String orderId;

    private String subject;

    private String courseName;

    //数量
    private BigDecimal amount;

}
