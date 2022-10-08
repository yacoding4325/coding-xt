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

    public CallResult<Object> findRoleById() {
        Integer roleId = this.adminUserParam.getRoleId();
        //查询角色
        AdminRole role = this.adminUserDomainRepository.findRoleId(roleId);

        //根据id查询选中的权限id 列表
        List<Integer> permissionIdList = this.adminUserDomainRepository.findPermissionIdListByRoleId(roleId);
        Map<String,Object> result = new HashMap<>();
        result.put("role",role);
        result.put("permissionIdList",permissionIdList);
        return CallResult.success(result);

    }

    public CallResult<Object> updateRole() {
        AdminRole role = new AdminRole();
        BeanUtils.copyProperties(this.adminUserParam,role);
        role.setId(this.adminUserParam.getRoleId());
        List<Integer> permissionIdList = this.adminUserParam.getPermissionIdList();
        this.adminUserDomainRepository.updateRole(role);
        Integer roleId = role.getId();
        //先删除关联关系
        this.adminUserDomainRepository.deleteRolePermissionByRoleId(roleId);
        this.adminUserDomainRepository.saveRolePermission(roleId,permissionIdList);
        return CallResult.success();
    }

    public CallResult<Object> findPermissionPage() {
        int page = this.adminUserParam.getPage();
        int pageSize = this.adminUserParam.getPageSize();
        Page<AdminPermission> adminPermissionPage = this.adminUserDomainRepository.findPermissionList(page,pageSize);
        ListModel listModel = new ListModel();
        listModel.setTotal((int) adminPermissionPage.getTotal());
        List<AdminPermission> result = adminPermissionPage.getRecords();
        listModel.setList(result);
        return CallResult.success(listModel);
    }

    public CallResult<Object> addPermission() {
        AdminPermission adminPermission = new AdminPermission();
        BeanUtils.copyProperties(adminUserParam,adminPermission);
        this.adminUserDomainRepository.savePermission(adminPermission);
        return CallResult.success();
    }

    public CallResult<Object> findPermissionById() {
        Integer permissionId = this.adminUserParam.getPermissionId();
        AdminPermission adminPermission = this.adminUserDomainRepository.findPermissionById(permissionId);
        return CallResult.success(adminPermission);
    }

    public CallResult<Object> updatePermission() {
        AdminPermission adminPermission = new AdminPermission();
        BeanUtils.copyProperties(adminUserParam,adminPermission);
        adminPermission.setId(adminUserParam.getPermissionId());
        this.adminUserDomainRepository.updatePermission(adminPermission);
        return CallResult.success();

    }

    public CallResult<Object> findPage() {
        int page = this.adminUserParam.getPage();
        int pageSize = this.adminUserParam.getPageSize();
        Page<AdminUser> adminUserPage = this.adminUserDomainRepository.findUserList(page,pageSize);
        ListModel listModel = new ListModel();
        listModel.setTotal((int) adminUserPage.getTotal());
        List<AdminUser> result = adminUserPage.getRecords();
        listModel.setList(result);
        return CallResult.success(listModel);
    }

    public CallResult<Object> roleAll() {
        List<AdminRole> adminRoleList = this.adminUserDomainRepository.findAllRole();
        return CallResult.success(adminRoleList);
    }

    public CallResult<Object> addUser() {

        /**
         * 1. 密码需要加密
         * 2. 角色存入关联表
         */
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(this.adminUserParam.getUsername());
        adminUser.setPassword(new BCryptPasswordEncoder().encode(this.adminUserParam.getPassword()));
        this.adminUserDomainRepository.saveUser(adminUser);
        List<Integer> roleIdList = this.adminUserParam.getRoleIdList();
        for (Integer roleId : roleIdList) {
            this.adminUserDomainRepository.saveUserRole(adminUser.getId(),roleId);
        }
        return CallResult.success();
    }

    public CallResult<Object> findUserById() {
        AdminUser adminUser = this.adminUserDomainRepository.findUserById(this.adminUserParam.getId());
        List<Integer> adminRoleIdListByUserId = this.adminUserDomainRepository.findAdminRoleIdListByUserId(this.adminUserParam.getId());
        Map<String,Object> result = new HashMap<>();
        result.put("user",adminUser);
        result.put("roleIdList",adminRoleIdListByUserId);
        return CallResult.success(result);
    }

    public CallResult<Object> update() {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(this.adminUserParam.getUsername());
        String newPassword = this.adminUserParam.getNewPassword();
        if (StringUtils.isNotBlank(newPassword)) {
            adminUser.setPassword(new BCryptPasswordEncoder().encode(this.adminUserParam.getNewPassword()));
        }
        adminUser.setId(this.adminUserParam.getId());
        this.adminUserDomainRepository.updateUser(adminUser);
        this.adminUserDomainRepository.deleteUserRoleByUserId(adminUser.getId());
        List<Integer> roleIdList = this.adminUserParam.getRoleIdList();
        for (Integer roleId : roleIdList) {
            this.adminUserDomainRepository.saveUserRole(adminUser.getId(),roleId);
        }
        return CallResult.success();
    }

    public CallResult<Object> findMenuPage() {
        int page = this.adminUserParam.getPage();
        int pageSize = this.adminUserParam.getPageSize();
        Page<AdminMenu> adminMenuPage = this.adminUserDomainRepository.findMenuPage(page,pageSize);
        ListModel listModel = new ListModel();
        listModel.setTotal((int) adminMenuPage.getTotal());
        List<AdminMenu> result = adminMenuPage.getRecords();
        listModel.setList(result);
        return CallResult.success(listModel);

    }

    public CallResult<Object> menuAll() {
        List<AdminMenu> menuAll = this.adminUserDomainRepository.findMenuAll();
        AdminMenu parent = new AdminMenu();
        parent.setId(0);
        parent.setId(0);
        parent.setMenuName("无父菜单");
        menuAll.add(parent);
        return CallResult.success(menuAll);
    }


    public CallResult<Object> saveMenu() {
        AdminMenu menu = new AdminMenu();
        BeanUtils.copyProperties(this.adminUserParam,menu);
        this.adminUserDomainRepository.saveMenu(menu);
        return CallResult.success();
    }

    public CallResult<Object> findMenuById() {
        AdminMenu menu = this.adminUserDomainRepository.findMenuById(this.adminUserParam.getMenuId());
        return CallResult.success(menu);
    }

    public CallResult<Object> updateMenu() {
        AdminMenu menu = new AdminMenu();
        BeanUtils.copyProperties(this.adminUserParam,menu);
        menu.setId(this.adminUserParam.getMenuId());
        this.adminUserDomainRepository.updateMenu(menu);
        return CallResult.success();
    }

    public CallResult<Object> userMenuList() {
        //要的数据是什么
        List<AdminMenuModel> adminMenuModelList = new ArrayList<>();
        //根据用户来去进行查询 角色 角色查询菜单
        Long userId = UserThreadLocal.get();
        List<Integer> roleIdList = this.adminUserDomainRepository.findAdminRoleIdListByUserId(userId);
        if (roleIdList.isEmpty()){
            return CallResult.success(adminMenuModelList);
        }
        List<AdminMenu> adminMenuList = this.adminUserDomainRepository.findMenuListByRoleIds(roleIdList);
        //构建树形菜单
        //首先第一级的菜单先构建出来
        for (AdminMenu menu : adminMenuList) {
            if (menu.getLevel() == 1){
                AdminMenuModel adminMenuModel = new AdminMenuModel();
                adminMenuModel.setId(menu.getId());
                adminMenuModel.setTitle(menu.getMenuName());
                adminMenuModel.setIcon("fa-user-md");
                adminMenuModel.setLevel(menu.getLevel());
                adminMenuModel.setChildren(childMenu(adminMenuModel,adminMenuList));
                adminMenuModelList.add(adminMenuModel);
            }
        }
        return CallResult.success(adminMenuModelList);
    }

    private List<AdminMenuModel> childMenu(AdminMenuModel adminMenuModel, List<AdminMenu> adminMenuList) {

        List<AdminMenuModel> adminMenuModelList = new ArrayList<>();
        if (adminMenuModel.getLevel() == 2){
            return adminMenuModelList;
        }
        for (AdminMenu menu : adminMenuList) {
            if (menu.getParentId().equals(adminMenuModel.getId()) && menu.getLevel() != 1) {
                AdminMenuModel amm = new AdminMenuModel();
                amm.setId(menu.getId());
                amm.setTitle(menu.getMenuName());
                amm.setIcon("fa-user-md");
                amm.setLevel(menu.getLevel());
                amm.setLinkUrl(menu.getMenuLink());
                amm.setChildren(childMenu(amm, adminMenuList));
                adminMenuModelList.add(amm);
            }
        }
        return adminMenuModelList;
    }
}
