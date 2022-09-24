package com.coding.xt.common.annontation;

import java.lang.annotation.*;

/**
 * @Author yaCoding
 * @create 2022-09-24 下午 9:07
 */

//注解的意义，需要登录信息，但是如果未登录，不进行拦截
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuth {
}

