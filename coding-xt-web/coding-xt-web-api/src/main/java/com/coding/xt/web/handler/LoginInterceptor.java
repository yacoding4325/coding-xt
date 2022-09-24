package com.coding.xt.web.handler;

import com.alibaba.fastjson.JSON;
import com.coding.xt.common.annontation.NoAuth;
import com.coding.xt.common.login.UserThreadLocal;
import com.coding.xt.common.model.BusinessCodeEnum;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.sso.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author yaCoding
 * @create 2022-09-24 下午 9:03
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    //duubo的sso提供的服务
    //服务提供方 指定的版本号是1.0.0
    //@DubboReference 实现了，通过注册中心，拿取提供方的地址，
    // 并进行网络调用（TCP，netty，hession），
    // 会将参数进行序列化传输，会将返回值反序列化处理
    @DubboReference(version = "1.0.0")
    private TokenService tokenService;

    //1. 实现登录拦截器，需要登录才能访问的接口，都会被拦截
    //2. 要从cookie中拿到对应的token
    //3. 根据token去做对应的认证，认证通过，拿到userId
    //4. 通过ThreadLocal将userId放入其中，后续的接口都可以通过threadLocal方便的拿到用户id

    //ThreadLocal ： 线程隔离的，多个线程之间，存放在threadLocal中的变量，不会被其他线程所获取和更改
    // 一个请求 就是一个线程，一个请求 controller，service，domain，dao代码
    //请求完成之后，threadLocal就会随着线程销毁
    //相比redis的好处，1. 省内存  3. redis获取信息 需要进行网络连接（开销极大）

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("-------------------login interceptor start-----------------------");
        log.info("request uri:{}",request.getRequestURI());
        log.info("request method:{}",request.getMethod());
        log.info("-------------------login interceptor end-----------------------");

        boolean isAuth = false;

        if (handler instanceof HandlerMethod){
            //代表拦截的方法是controller的方法
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            isAuth = handlerMethod.hasMethodAnnotation(NoAuth.class);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return handlerResponse(response, isAuth);
        }
        String token = null;
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if ("t_token".equals(name)){
                token = cookie.getValue();
            }
        }

        if (StringUtils.isBlank(token)){
            return handlerResponse(response, isAuth);
        }
        Long userId = tokenService.checkToken(token);
        if (userId == null){
            return handlerResponse(response, isAuth);
        }
        UserThreadLocal.put(userId);
        return true;
    }

    private boolean handlerResponse(HttpServletResponse response, boolean isAuth) {
        if (isAuth){
            return true;
        }else {
            returnJson(response);
            return false;
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //用完threadLocal之后 其中的数据 删除，以防出现内存泄漏问题
        //threadLocal 内存泄漏  面试问题
        UserThreadLocal.remove();
    }

    private void returnJson(HttpServletResponse response){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            CallResult callResult = CallResult.fail(BusinessCodeEnum.NO_LOGIN.getCode(),"您的登录已失效，请重新登录");
            writer.print(JSON.toJSONString(callResult));
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }

}
