package com.coding.xt.admin.dao.data;

import lombok.Data;
//管理员权限
@Data
public class AdminPermission {

    private Integer id;

    private String permissionName;

    private String permissionDesc;

    private String permissionPath;

    private String permissionKeywords;

}
