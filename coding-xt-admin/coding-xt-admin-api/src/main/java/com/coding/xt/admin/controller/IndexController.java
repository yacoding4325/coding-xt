package com.coding.xt.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @Author yaCoding
 * @create 2022-10-08 下午 9:29
 */

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "redirect:/xt/index";
    }

    public static void main(String[] args) {
        System.out.println(new Date(1638094136959L));
    }

}

