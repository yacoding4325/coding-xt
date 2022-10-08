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
}
