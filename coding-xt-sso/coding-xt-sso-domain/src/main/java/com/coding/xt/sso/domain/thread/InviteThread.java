package com.coding.xt.sso.domain.thread;

import com.coding.xt.common.enums.InviteType;
import com.coding.xt.common.utils.AESUtils;
import com.coding.xt.pojo.Invite;
import com.coding.xt.sso.dao.data.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 4:35
 */

public class InviteThread {


    @Async("taskExecutor")
    public void fillInvite(List<Map<String,String>> inviteMapList, User user) {
        for (Map<String,String> map : inviteMapList) {
            //有推荐信息，构建邀请信息
            Invite invite = new Invite();
            invite.setInviteInfo(user.getUnionId());
            invite.setInviteStatus(0);
            invite.setInviteTime(System.currentTimeMillis());
            invite.setInviteType(InviteType.LOGIN.getCode());
            invite.setInviteUserId(user.getId());
            invite.setUserId(Long.parseLong(AESUtils.decrypt(map.get("userId"))));
            invite.setBillType(map.get("billType"));
            invite.setCreateTime(System.currentTimeMillis());
        }
    }



}
