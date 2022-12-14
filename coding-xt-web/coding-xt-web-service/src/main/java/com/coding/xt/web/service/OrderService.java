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

    /**
     * 微信支付 --根据订单id和类型 审生成支付二维码
     * @param orderParam
     * @return
     */
    CallResult wxPay(OrderParam orderParam);

    /**
     * 支付回调--通知单  处理订单成功的相关操作
     * @param xmlData
     * @return
     */
    CallResult notifyOrder(String xmlData);

    /**
     * 查询订单
     * @param orderParam
     * @return
     */
    CallResult findOrder(OrderParam orderParam);

    /**
     * 订单列表 --查询列表
     * @param orderParam
     * @return
     */
    CallResult orderList(OrderParam orderParam);
}
