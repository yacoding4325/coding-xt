package com.coding.xt.sso.api;


import com.coding.xt.common.model.CallResult;
import com.coding.xt.sso.model.params.LoginParam;
import com.coding.xt.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author yaCoding
 * @create 2022-09-17 上午 11:20
 */

///api/sso/login/getQRCodeUrl

//controller注解 默认是调整页面的
@Controller
@RequestMapping("login")
public class LoginApi {

    @Autowired
    private LoginService loginService;

    @PostMapping("getQRCodeUrl")
    @ResponseBody
    public CallResult getQRCodeUrl(){
        //controller 职责 接收参数  一个处理结果
        return loginService.getQRCodeUrl();
    }

    //redirect_uri?code=CODE&state=STATE
    @GetMapping("wxLoginCallBack")
    public String wxLoginCallBack(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String code,
                                  String state){
        //为了service层统一，所有的api层的参数处理 ，都放入loginParams中
        LoginParam loginParam = new LoginParam();
        loginParam.setCode(code);
        loginParam.setState(state);
        loginParam.setRequest(request);
        //后续 登录成功之后，要生成token，提供给前端，把token放入对应的cookie
        // response.addCookie();
        loginParam.setResponse(response);
        CallResult callResult = loginService.wxLoginCallBack(loginParam);
        if (callResult.isSuccess()){
            return "redirect:http://www.lzxtedu.com/course";
        }else{
            return "redirect:http://www.lzxtedu.com";
        }
    }
    //测试 需要网络，回调的时候，微信方 是通过外网进行的一个访问，内网穿透
    //将本地一个端口，通过内网穿透的工具 ，暴露到外网链接
    //微信有安全配置 必须得是配置的域名才行
    //http://a4tuaki.nat.ipyingshe.com/api/sso/login/authorize
    @RequestMapping("authorize")
    public String authorize(){
        String redirectUrl = this.loginService.authorize();
        return "redirect:"+ redirectUrl;
    }

    //redirect_uri?code=CODE&state=STATE
    @GetMapping("wxGzhLoginCallBack")
    public String wxGzhLoginCallBack(HttpServletRequest request,
                                     HttpServletResponse response,
                                     String code,
                                     String state){
        //为了service层统一，所有的api层的参数处理 ，都放入loginParams中
        LoginParam loginParam = new LoginParam();
        loginParam.setCode(code);
        loginParam.setState(state);
        loginParam.setRequest(request);
        //后续 登录成功之后，要生成token，提供给前端，把token放入对应的cookie
        // response.addCookie();
        loginParam.setResponse(response);
        CallResult callResult = loginService.wxGzhLoginCallBack(loginParam);
        if (callResult.isSuccess()){
            return "redirect:http://a4tuaki.nat.ipyingshe.com/course";
        }else{
            return "redirect:http://a4tuaki.nat.ipyingshe.com";
        }
    }
}
