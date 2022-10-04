package com.coding.xt.web.api;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.OrderParam;
import com.coding.xt.web.service.OrderService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
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

    //微信支付
    @PostMapping("wxPay")
    public CallResult wxPay(@RequestBody OrderParam orderParam){
        return orderService.wxPay(orderParam);
    }

    //支付回调--控制
    @PostMapping("notify")
    public String notifyOrder(@RequestBody String xmlData){
        System.out.println("notify 数据："+xmlData);
        CallResult callResult = orderService.notifyOrder(xmlData);
        if (callResult.isSuccess()){
            return WxPayNotifyResponse.success("成功");
        }
        return WxPayNotifyResponse.fail("失败");
    }

    //制作订单 -- 查找订单
    @PostMapping("findOrder")
    public CallResult findOrder(@RequestBody OrderParam orderParam){
        return orderService.findOrder(orderParam);
    }

    //订单列表
    @PostMapping(value = "orderList")
    public CallResult orderList(@RequestBody OrderParam orderParam){
        return orderService.orderList(orderParam);
    }

}
