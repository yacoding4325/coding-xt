package com.coding.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.admin.dao.data.AdminMenu;
import com.coding.xt.admin.dao.data.AdminPermission;
import com.coding.xt.admin.dao.data.AdminRole;
import com.coding.xt.admin.dao.data.AdminUser;
import com.coding.xt.admin.domain.repository.AdminUserDomainRepository;
import com.coding.xt.admin.model.AdminMenuModel;
import com.coding.xt.admin.model.AdminUserModel;
import com.coding.xt.admin.params.AdminUserParam;
import com.coding.xt.common.login.UserThreadLocal;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.model.ListModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:21
 */

public class AdminUserDomain {

    private AdminUserDomainRepository adminUserDomainRepository;
    private AdminUserParam adminUserParam;

    public AdminUserDomain(AdminUserDomainRepository adminUserDomainRepository, AdminUserParam adminUserParam) {
        this.adminUserDomainRepository = adminUserDomainRepository;
        this.adminUserParam = adminUserParam;
    }

    //查找用户名
    public AdminUserModel findUserByUsername() {
        AdminUser adminUser = adminUserDomainRepository.findUserByUsername(this.adminUserParam.getUsername());
        AdminUserModel adminUserModel = new AdminUserModel();
        BeanUtils.copyProperties(adminUser,adminUserModel);
        return adminUserModel;
    }

    public CallResult<Boolean> auth(String requestURI, Long userId) {
        //用户和角色关联表 直接通过关联表 查询角色的id列表
        List<Integer> roleIdList = adminUserDomainRepository.findRoleIdListByUserId(userId);
        if (roleIdList.isEmpty()) {
            return CallResult.fail(false);
        }
        //角色和权限的关联表 查询到 权限id列表
        List<Integer> permissionIdList = adminUserDomainRepository.findPermissionIdListByRoleIds(roleIdList);
        if (permissionIdList.isEmpty()) {
            return CallResult.fail(false);
        }
        List<AdminPermission> permissionList = adminUserDomainRepository.findPermissionByIds(permissionIdList);
        for (AdminPermission adminPermission : permissionList) {
            // /course/**
            String permissionPath = adminPermission.getPermissionPath();
            if (new AntPathMatcher().match(permissionPath,requestURI)){
                return CallResult.success(true);
            }
        }
        return CallResult.fail(false);
    }

    public CallResult<Object> findRolePage() {

        int page = this.adminUserParam.getPage();
        int pageSize = this.adminUserParam.getPageSize();
        Page<AdminRole> adminRolePage = this.adminUserDomainRepository.findRoleList(page,pageSize);
        ListModel listModel = new ListModel();
        listModel.setTotal((int) adminRolePage.getTotal());
        List<AdminRole> result = adminRolePage.getRecords();
        listModel.setList(result);
        return CallResult.success(listModel);

    }

    public CallResult<Object> permissionAll() {
        List<AdminPermission> allPermission = this.adminUserDomainRepository.findAllPermission();
        return CallResult.success(allPermission);
    }

    public CallResult<Object> add() {
        AdminRole role = new AdminRole();
        BeanUtils.copyProperties(this.adminUserParam,role);
        List<Integer> permissionIdList = this.adminUserParam.getPermissionIdList();
        this.adminUserDomainRepository.saveRole(role);
        Integer roleId = role.getId();
        this.adminUserDomainRepository.saveRolePermission(roleId,permissionIdList);
        return CallResult.success();
    }
}
