package com.coding.xt.admin.service;

import com.coding.xt.admin.model.AdminUserModel;
import com.coding.xt.admin.params.AdminUserParam;
import com.coding.xt.common.model.CallResult;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:10
 */
//管理员登录接口
public interface AdminUserService {

    /**
     * 查询用户--用户按用户名
     * @param username
     * @return
     */
    AdminUserModel findUserByUsername(String username);

    /**
     * 身份验证
     * @param requestURI
     * @param id
     * @return
     */
    boolean auth(String requestURI, Long id);

    /**
     * 查找角色页面
     * @param adminUserParam
     * @return
     */
    CallResult findRolePage(AdminUserParam adminUserParam);

}
