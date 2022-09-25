package com.coding.xt.common.cache;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author yaCoding
 * @create 2022-09-25 下午 8:41
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CacheAspect.class)
public @interface EnableCache {

}
