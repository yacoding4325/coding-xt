package com.coding.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.admin.dao.*;
import com.coding.xt.admin.dao.data.*;
import com.coding.xt.admin.domain.AdminUserDomain;
import com.coding.xt.admin.params.AdminUserParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:22
 */
@Component
public class AdminUserDomainRepository {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private AdminUserRoleMapper adminUserRoleMapper;

    @Resource
    private AdminPermissionMapper adminPermissionMapper;

    @Resource
    private AdminRolePermissionMapper adminRolePermissionMapper;

    @Resource
    private AdminMenuMapper adminMenuMapper;

    @Resource
    private AdminRoleMenuMapper adminRoleMenuMapper;

    @Resource
    private AdminRoleMapper adminRoleMapper;


    public AdminUserDomain createDomain(AdminUserParam adminUserParam) {
        return new AdminUserDomain(this,adminUserParam);
    }

    public AdminUser findUserByUsername(String username) {
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        //limit 1优化sql 防止进行全表查询
        queryWrapper.eq(AdminUser::getUsername,username).last("limit 1");
        return adminUserMapper.selectOne(queryWrapper);
    }

    //按用户ID查找角色ID列表
    public List<Integer> findRoleIdListByUserId(Long userId) {
        LambdaQueryWrapper<AdminUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminUserRole::getUserId,userId);
        List<AdminUserRole> adminUserRoleList = adminUserRoleMapper.selectList(queryWrapper);
        return adminUserRoleList.stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
    }


    public List<AdminPermission> findPermissionByIds(List<Integer> permissionIdList) {
        LambdaQueryWrapper<AdminPermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AdminPermission::getId,permissionIdList);
        return adminPermissionMapper.selectList(queryWrapper);
    }

    public List<Integer> findPermissionIdListByRoleIds(List<Integer> roleIdList) {
        LambdaQueryWrapper<AdminRolePermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AdminRolePermission::getRoleId,roleIdList);
        List<AdminRolePermission> adminRolePermissions = this.adminRolePermissionMapper.selectList(queryWrapper);

        return adminRolePermissions.stream().map(AdminRolePermission::getPermissionId).collect(Collectors.toList());
    }

    public Page<AdminRole> findRoleList(int page, int pageSize) {
        return adminRoleMapper.selectPage(new Page<>(page,pageSize),Wrappers.lambdaQuery());
    }

    public List<AdminPermission> findAllPermission() {
        return adminPermissionMapper.selectList(Wrappers.lambdaQuery());
    }

    public void saveRole(AdminRole role) {
        this.adminRoleMapper.insert(role);
    }

    public void saveRolePermission(Integer roleId, List<Integer> permissionIdList) {
        for (Integer permissionId: permissionIdList) {
            AdminRolePermission adminRolePermission = new AdminRolePermission();
            adminRolePermission.setRoleId(roleId);
            adminRolePermission.setPermissionId(permissionId);
            this.adminRolePermissionMapper.insert(adminRolePermission);
        }
    }

    public AdminRole findRoleId(Integer roleId) {
        return adminRoleMapper.selectById(roleId);
    }

    public List<Integer> findPermissionIdListByRoleId(Integer roleId) {
        LambdaQueryWrapper<AdminRolePermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminRolePermission::getRoleId,roleId);
        return adminRolePermissionMapper.selectList(queryWrapper).stream().map(AdminRolePermission::getPermissionId).collect(Collectors.toList());
    }

    public void updateRole(AdminRole role) {
        this.adminRoleMapper.updateById(role);
    }

    public void deleteRolePermissionByRoleId(Integer roleId) {
        LambdaQueryWrapper<AdminRolePermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminRolePermission::getRoleId,roleId);
        this.adminRolePermissionMapper.delete(queryWrapper);
    }

    public void savePermission(AdminPermission adminPermission) {
        adminPermissionMapper.insert(adminPermission);
    }

    public void updatePermission(AdminPermission adminPermission) {
        adminPermissionMapper.updateById(adminPermission);
    }

    public AdminPermission findPermissionById(Integer permissionId) {
        return adminPermissionMapper.selectById(permissionId);
    }

    public Page<AdminPermission> findPermissionList(int page, int pageSize) {
        return adminPermissionMapper.selectPage(new Page<>(page,pageSize),Wrappers.lambdaQuery());
    }

    public Page<AdminUser> findUserList(int page, int pageSize) {
        return adminUserMapper.selectPage(new Page<>(page,pageSize),Wrappers.lambdaQuery());
    }

    public List<AdminRole> findAllRole() {
        return adminRoleMapper.selectList(Wrappers.lambdaQuery());
    }

    public void saveUser(AdminUser adminUser) {
        adminUserMapper.insert(adminUser);
    }

    public void saveUserRole(Long userId, Integer roleId) {
        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setRoleId(roleId);
        adminUserRole.setUserId(userId);
        this.adminUserRoleMapper.insert(adminUserRole);
    }

    public AdminUser findUserById(Long id) {
        return adminUserMapper.selectById(id);
    }

    public void updateUser(AdminUser adminUser) {
        adminUserMapper.updateById(adminUser);
    }

    public void deleteUserRoleByUserId(Long userId) {
        LambdaQueryWrapper<AdminUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminUserRole::getUserId,userId);
        this.adminUserRoleMapper.delete(queryWrapper);
    }

    //按用户ID查找管理员角色ID列表
    public List<Integer> findAdminRoleIdListByUserId(Long adminUserId) {
        LambdaQueryWrapper<AdminUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AdminUserRole::getUserId, adminUserId);
        queryWrapper.select(AdminUserRole::getRoleId);
        List<AdminUserRole> adminUserRoleList = this.adminUserRoleMapper.selectList(queryWrapper);
        List<Integer> roleIdList = adminUserRoleList.stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
        return roleIdList;
    }

    //查找菜单页
    public Page<AdminMenu> findMenuPage(int page, int pageSize) {
        return adminMenuMapper.selectPage(new Page<>(page,pageSize),Wrappers.lambdaQuery());
    }


    public List<AdminMenu> findMenuAll() {
        return adminMenuMapper.selectList(Wrappers.lambdaQuery());
    }

    public void saveMenu(AdminMenu menu) {
        adminMenuMapper.insert(menu);
    }

    public AdminMenu findMenuById(Integer menuId) {
        return adminMenuMapper.selectById(menuId);
    }

    public void updateMenu(AdminMenu menu) {
        adminMenuMapper.updateById(menu);
    }

    public List<AdminMenu> findMenuListByRoleIds(List<Integer> roleIdList) {
        LambdaQueryWrapper<AdminRoleMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AdminRoleMenu::getRoleId,roleIdList);
        List<AdminRoleMenu> adminRoleMenus = adminRoleMenuMapper.selectList(queryWrapper);
        List<Integer> menuIdList = adminRoleMenus.stream().map(AdminRoleMenu::getMenuId).collect(Collectors.toList());
        LambdaQueryWrapper<AdminMenu> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.in(AdminMenu::getId,menuIdList);
        queryWrapper1.orderByAsc(AdminMenu::getMenuSeq);
        return adminMenuMapper.selectList(queryWrapper1);
    }
}
