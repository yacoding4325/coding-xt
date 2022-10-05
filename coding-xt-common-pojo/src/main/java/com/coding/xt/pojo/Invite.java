package com.coding.xt.pojo;

import lombok.Data;

@Data
public class Invite {

    private Long id;

    private Long userId;

    private Long inviteUserId;

    private Integer inviteType;

    private String inviteInfo;

    private Long inviteTime;

    private Integer inviteStatus;

    private String billType;

    private Long createTime;

}
