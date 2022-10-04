package com.coding.xt.web.config;

import com.coding.xt.common.cache.EnableCache;
import com.coding.xt.common.service.EnableService;
import com.coding.xt.common.wx.config.EnableWxPay;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:07
 */

/**
 * 在InitConfig中，使用了@ComponentScan注解，这时候需要想一个问题，
 * 如果包多了，是不是每个包都要扫一遍，是不是每个包都要记住呢？
 * ---上述的注解使用@Enable开头的注解代替
 */
@Configuration
//@ComponentScan({"com.coding.xt.common.cache","com.coding.xt.common.service"})
@EnableCache
@EnableService
@EnableWxPay
public class InitConfig {
}
