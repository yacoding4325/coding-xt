package com.coding.xt.sso.domain;

import com.coding.xt.pojo.Invite;
import com.coding.xt.sso.domain.repository.InviteDomainRepository;
import com.coding.xt.sso.model.params.InviteParam;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 4:41
 */

public class InviteDomain {

    private InviteDomainRepository inviteDomainRepository;
    private InviteParam inviteParam;

    public InviteDomain(InviteDomainRepository inviteDomainRepository, InviteParam inviteParam) {
        this.inviteDomainRepository = inviteDomainRepository;
        this.inviteParam = inviteParam;
    }

    public void save(Invite invite) {
        inviteDomainRepository.save(invite);
    }
}
