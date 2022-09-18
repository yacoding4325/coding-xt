package com.coding.xt.sso.model;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 10:44
 */

import lombok.Data;

@Data
public class UserModel {

    private String nickname;

    private Integer sex;

    private String city;

    private String province;

    private String country;

    private String headImageUrl;

    private String mobile;

    private String name;

    private String inviteUrl;

    private String school;

    private String area;

    private String grade;
}
