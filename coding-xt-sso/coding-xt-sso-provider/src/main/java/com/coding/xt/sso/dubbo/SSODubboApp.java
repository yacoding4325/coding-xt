package com.coding.xt.sso.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author yaCoding
 * @create 2022-09-24 下午 6:15
 */
//集成sso的Dubbo启动类
@SpringBootApplication
public class SSODubboApp {

    public static void main(String[] args) {
        SpringApplication.run(SSODubboApp.class,args);
    }

}
