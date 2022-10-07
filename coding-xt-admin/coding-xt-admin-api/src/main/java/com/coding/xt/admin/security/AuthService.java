package com.coding.xt.admin.security;

import com.coding.xt.admin.model.AdminUserModel;
import com.coding.xt.admin.service.AdminUserService;
import com.coding.xt.common.login.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author yaCoding
 * @create 2022-10-07 下午 4:01
 */

@Component
public class AuthService {

    @Autowired
    private AdminUserService adminUserService;

    @Value("${server.servlet.context-path}")
    private String contextPath;
    /**
     *
     * @param request
     * @param authentication
     * @return true代表权限通过 false代表权限不通过
     */
    public boolean auth(HttpServletRequest request, Authentication authentication){

        /**
         * 1. 判断当前的用户是否登录 如果未登录或者登录失效 返回false
         * 2. 拿到登录用户的信息，根据用户信息 去查询用户 根据用户id查询角色
         * 3. 根据角色去查询对应的权限
         */
        Object principal = authentication.getPrincipal();
        if ("anonymousUser".equals(principal)){
            //匿名用户 当前用户未登录
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        AdminUserModel adminUserModel = this.adminUserService.findUserByUsername(username);

        //请求链接 /lzadmin/course/findPage
        String requestURI = request.getRequestURI();
        requestURI = requestURI.replace(contextPath,"");
        Long id = adminUserModel.getId();
        UserThreadLocal.put(id);
        if ("/user/menu/userMenuList".equals(requestURI)){
            return true;
        }
        return adminUserService.auth(requestURI,id);
    }

}
