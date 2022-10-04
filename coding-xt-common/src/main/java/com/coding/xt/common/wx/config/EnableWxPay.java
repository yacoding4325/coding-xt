package com.coding.xt.common.wx.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author yaCoding
 * @create 2022-10-04 上午 8:22
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WxPayConfiguration.class)

public @interface EnableWxPay {
}
