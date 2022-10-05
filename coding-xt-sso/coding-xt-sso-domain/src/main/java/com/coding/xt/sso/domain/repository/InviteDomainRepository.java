package com.coding.xt.sso.domain.repository;

import com.coding.xt.pojo.Invite;
import com.coding.xt.sso.dao.InviteMapper;
import com.coding.xt.sso.domain.InviteDomain;
import com.coding.xt.sso.model.params.InviteParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 4:41
 */


@Component
public class InviteDomainRepository {

    @Resource
    private InviteMapper inviteMapper;

    public InviteDomain createDomain(InviteParam inviteParam) {
        return new InviteDomain(this,inviteParam);
    }

    public void save(Invite invite) {
        inviteMapper.insert(invite);
    }
}
