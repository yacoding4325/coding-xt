package com.coding.xt.admin.service;

import com.coding.xt.admin.model.AdminUserModel;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:10
 */
//管理员登录接口
public interface AdminUserService {
    AdminUserModel findUserByUsername(String username);

}
