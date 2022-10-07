package com.coding.xt.admin.params;

import lombok.Data;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:14
 */
//管理员用户参数
@Data
public class AdminUserParam {

    private String username;

    private String password;

    private String newPassword;

    private int page;

    private int pageSize;

    private Long id;

    private Integer roleId;

    private Integer permissionId;

    private Integer menuId;

    private String roleName;

    private String roleDesc;

    private String roleKeywords;

    private List<Integer> permissionIdList;

    private String permissionName;

    private String permissionDesc;

    private String permissionKeywords;

    private String permissionPath;

    private List<Integer> roleIdList;

    private List<Integer> menuIdList;

    private String menuName;

    private String menuDesc;

    private String menuKeywords;

    private Integer parentId;

    private Integer level;

    private String menuLink;

    private Integer menuSeq;

}
