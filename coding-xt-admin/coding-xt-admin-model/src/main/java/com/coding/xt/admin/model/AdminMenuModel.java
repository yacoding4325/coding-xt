package com.coding.xt.admin.model;

import lombok.Data;

import java.util.List;

@Data
public class AdminMenuModel {

    private String path;

    private Integer id;

    private String title;

    private String linkUrl;

    private String icon;

    private Integer level;

    private List<AdminMenuModel> children;

    public String getPath(){
        return id.toString();
    }

}
