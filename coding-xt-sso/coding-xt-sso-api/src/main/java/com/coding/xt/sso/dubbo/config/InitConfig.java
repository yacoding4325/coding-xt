package com.coding.xt.sso.dubbo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 9:35
 */

@Configuration
@ComponentScan({"com.coding.xt.common.wx.config","com.coding.xt.common.service"})
public class InitConfig {
}

