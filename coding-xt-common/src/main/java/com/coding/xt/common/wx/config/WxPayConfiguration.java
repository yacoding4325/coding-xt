package com.coding.xt.common.wx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author yaCoding
 * @create 2022-10-04 上午 7:50
 */
@Component
public class WxPayConfiguration {

    @Value("${wx.pay.appId}")
    public String payAppId;

    @Value("${wx.pay.mchId}")
    public String mchId;

    @Value("${wx.pay.mchKey}")
    public String mchKey;

    @Value("${wx.notify.url}")
    public String wxNotifyUrl;

}
