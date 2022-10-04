package com.coding.web.domain.pay;

import com.coding.xt.common.wx.config.WxPayConfiguration;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

/**
 * @Author yaCoding
 * @create 2022-10-03 下午 1:46
 */

public class WxPayDomain {

    private WxPayConfiguration wxPayConfig;

    public WxPayDomain(WxPayConfiguration config) {
        this.wxPayConfig = config;
    }

    public WxPayService getWxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxPayConfig.payAppId);
        payConfig.setMchId(wxPayConfig.mchId);
        payConfig.setMchKey(wxPayConfig.mchKey);
        payConfig.setKeyPath("classpath:apiclient_cert.p12");
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}
