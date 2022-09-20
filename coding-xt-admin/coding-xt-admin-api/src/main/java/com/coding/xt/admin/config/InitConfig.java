package com.coding.xt.admin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 8:48
 */
//扫描的路劲
@Configuration
@ComponentScan({"com.coding.xt.common.service"})
public class InitConfig {
}
