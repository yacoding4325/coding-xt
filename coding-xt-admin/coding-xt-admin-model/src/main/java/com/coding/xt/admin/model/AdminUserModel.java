package com.coding.xt.admin.model;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:11
 */

///登录用户的模型
@Data
public class AdminUserModel {

    private Long id;
    private String username;
    private String password;

}
