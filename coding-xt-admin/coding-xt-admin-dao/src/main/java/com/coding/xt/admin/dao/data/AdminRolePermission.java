package com.coding.xt.admin.dao.data;

import lombok.Data;

//管理员角色
@Data
public class AdminRolePermission {

    private Long id;

    private Integer roleId;

    private Integer permissionId;

}
