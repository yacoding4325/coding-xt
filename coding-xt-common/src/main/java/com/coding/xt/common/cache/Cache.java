package com.coding.xt.common.cache;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    /**
     * 缓存前缀名称
     * @return
     */
    String name() default "";

    /**
     * 过期时间 默认是60s
     * @return
     */
    int time() default 60;

    /**
     * 是否需要当前的用户作为key的一部分
     * @return
     */
    boolean hasUser() default false;


}
