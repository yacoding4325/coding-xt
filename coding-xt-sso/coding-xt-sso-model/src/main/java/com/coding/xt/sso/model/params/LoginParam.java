package com.coding.xt.sso.model.params;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 9:47
 */
@Data
public class LoginParam {

    private String username;
    private String password;
    //wx回调的传参
    private String code;
    private String state;
    private HttpServletResponse response;
    private HttpServletRequest request;

    private String cookieUserId;

}
