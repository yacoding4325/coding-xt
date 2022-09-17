package com.coding.xt.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author yaCoding
 * @create 2022-09-16 下午 10:15
 */
//默认的扫包路径是 当前包 以及子包
@SpringBootApplication
public class SSOApp {

    public static void main(String[] args) {
        SpringApplication.run(SSOApp.class,args);
    }
}
