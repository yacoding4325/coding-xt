package com.coding.xt.web.service;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.OrderParam;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:45
 */

public interface OrderService {

    /**
     * 提交订单信息
     * @param orderParam
     * @return
     */
    CallResult submitOrder(OrderParam orderParam);

}
