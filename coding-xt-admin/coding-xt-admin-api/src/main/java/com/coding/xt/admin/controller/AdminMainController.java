package com.coding.xt.admin.controller;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.admin.params.AdminUserParam;
import com.coding.xt.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * @Author yaCoding
 * @create 2022-10-08 下午 9:09
 */
//通过模板展示页面
@Controller
@RequestMapping("xt")
public class AdminMainController {

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("index")
    public ModelAndView mainPage(){
        ModelAndView modelAndView = new ModelAndView();
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails){
            UserDetails userDetails = (UserDetails) principal;
            modelAndView.addObject("username",userDetails.getUsername());
        }
        CallResult callResult = adminUserService.userMenuList(new AdminUserParam());
        modelAndView.addObject("menuList",callResult.getResult());
        modelAndView.setViewName("main");
        return modelAndView;
    }

    @RequestMapping("test")
    public ModelAndView test(Integer flag,String name){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("flag",flag);
        modelAndView.addObject("user",name);
        modelAndView.addObject("date",new Date());
        modelAndView.setViewName("test");
        return modelAndView;
    }

}
