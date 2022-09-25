package com.coding.xt.common.service;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 8:43
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ServiceTemplateImpl.class)
public @interface EnableService {
}