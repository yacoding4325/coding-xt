package com.coding.xt.admin.dao.data;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-10-07 下午 3:53
 */

//管理员角色
@Data
public class AdminRole {

    private Integer id;

    private String roleName;

    private String roleDesc;

    private String roleKeywords;

}

