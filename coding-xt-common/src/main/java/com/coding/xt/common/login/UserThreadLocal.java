package com.coding.xt.common.login;

/**
 * @Author yaCoding
 * @create 2022-09-16 下午 9:58
 */

public class UserThreadLocal {

    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    public static void put(Long useId) {
        LOCAL.set(useId);
    }

    public static Long get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
