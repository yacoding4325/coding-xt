package com.coding.xt.web.api;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.OrderParam;
import com.coding.xt.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:45
 */
@RestController
@RequestMapping("order")
public class OrderApi {

    @Autowired
    private OrderService orderService;

    //提交订单
    @PostMapping("submitOrder")
    public CallResult submitOrder(@RequestBody OrderParam orderParam){
        return orderService.submitOrder(orderParam);
    }

}
