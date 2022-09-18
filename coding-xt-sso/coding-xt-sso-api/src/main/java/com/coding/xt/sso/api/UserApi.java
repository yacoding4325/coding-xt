package com.coding.xt.sso.api;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.sso.model.params.UserParam;
import com.coding.xt.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yaCoding
 * @create 2022-09-18 下午 2:52
 */
@RestController
@RequestMapping("user")
public class UserApi {

    @Autowired
    private UserService userService;


    @RequestMapping("userInfo")
    public CallResult userInfo(){
        UserParam userParam = new UserParam();
        return userService.userInfo(userParam);
    }
}
