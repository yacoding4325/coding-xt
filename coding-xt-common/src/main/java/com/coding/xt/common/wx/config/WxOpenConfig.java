package com.coding.xt.common.wx.config;

import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yaCoding
 * @create 2022-09-16 下午 10:04
 */

@Configuration
@Data
public class WxOpenConfig {

    @Value("${wx.open.config.appid}")
    private String loginAppid;
    @Value("${wx.open.config.secret}")
    private String loginSecret;
    @Value("${wx.open.config.csrfKey}")
    public String csrfKey;
    @Value("${wx.open.config.redirectUrl}")
    public String redirectUrl;
    @Value("${wx.open.config.scope}")
    public String scope;
    @Value("${wx.open.config.mobile.redirectUrl}")
    public String mobileRedirectUrl;
    @Value("${wx.pay.appId}")
    private String appid;
    @Value("${wx.open.config.pay.secret}")
    private String secret;

    @Bean
    public WxMpService wxMpService() {
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(loginAppid);
        wxMpConfigStorage.setSecret(loginSecret);
        service.setWxMpConfigStorage(wxMpConfigStorage);
        return service;
    }

    @Bean("wxMpServiceGzh")
    public WxMpService wxMpServiceGzh() {
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(appid);
        wxMpConfigStorage.setSecret(secret);
        service.setWxMpConfigStorage(wxMpConfigStorage);
        return service;
    }
}
