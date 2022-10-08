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

    /**
     * 查询所有权限
     * @return
     */
    CallResult permissionAll();

    /**
     * 添加角色
     * @param adminUserParam
     * @return
     */
    CallResult add(AdminUserParam adminUserParam);

    /**
     * 查找角色ID
     * @param adminUserParam
     * @return
     */
    CallResult findRoleById(AdminUserParam adminUserParam);

    /**
     * 更新角色
     * @param adminUserParam
     * @return
     */
    CallResult updateRole(AdminUserParam adminUserParam);

    CallResult findPermissionPage(AdminUserParam adminUserParam);

    CallResult addPermission(AdminUserParam adminUserParam);

    CallResult findPermissionById(AdminUserParam adminUserParam);

    CallResult updatePermission(AdminUserParam adminUserParam);
}
