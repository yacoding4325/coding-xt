package com.coding.xt.sso.service;


import com.coding.xt.common.model.CallResult;
import com.coding.xt.sso.model.params.UserParam;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 9:48
 */

public interface UserService {


    /**
     * 获取用户登录信息
     * @return
     */
    CallResult userInfo(UserParam userParam);


}
