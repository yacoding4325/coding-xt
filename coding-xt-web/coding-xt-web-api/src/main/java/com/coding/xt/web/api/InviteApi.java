package com.coding.xt.web.api;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.BillParam;
import com.coding.xt.web.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 3:43
 */
@Controller
@RequestMapping("i")
public class InviteApi {

    @Autowired
    private BillService billService;

    @RequestMapping(value = "all")
    @ResponseBody
    public CallResult all(){
        return billService.all(new BillParam());
    }

    @RequestMapping("gen")
    @ResponseBody
    public CallResult gen(@RequestBody BillParam billParam){
        return billService.gen(billParam);
    }

    @RequestMapping("u/{billType}/{id}")
    public String url(HttpServletRequest request,
                      HttpServletResponse response,
                      @PathVariable("billType") String billType,
                      @PathVariable("id") String id){
        if(billType != null && id != null){
            //将其信息 埋入cookie，后续此用户做任何操作，我们可以判断cookie当中是否有推荐人信息
            Cookie cookie = new Cookie("_i_ga_b_"+billType, id);
            //cookie过期时间
            cookie.setMaxAge(3 * 24 * 3600);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        //当用户访问推广链接的时候，需要跳转到应用首页
        String ua = request.getHeader("user-agent").toLowerCase();
        if (ua.indexOf("micromessenger") > 0){
            //微信浏览器 跳转微信登录
            return "redirect:/api/sso/login/authorize";
        }
        return "redirect:http://www.mszlu.com";
    }

}
