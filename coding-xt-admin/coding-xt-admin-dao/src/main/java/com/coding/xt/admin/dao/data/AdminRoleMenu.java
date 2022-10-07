package com.coding.xt.admin.dao.data;

import lombok.Data;

//管理员角色菜单
@Data
public class AdminRoleMenu {

    private Long id;

    private Integer roleId;

    private Integer menuId;

}