package com.coding.xt.sso.service.impl;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import com.coding.xt.sso.domain.UserDomain;
import com.coding.xt.sso.domain.repository.UserDomainRepository;
import com.coding.xt.sso.model.params.UserParam;
import com.coding.xt.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yaCoding
 * @create 2022-09-18 下午 2:54
 */
@Service
public class UserServiceImpl extends AbstractService implements UserService {

    @Autowired
    private UserDomainRepository userDomainRepository;

    @Override
    public CallResult userInfo(UserParam userParam) {
        UserDomain userDomain = userDomainRepository.createDomain(userParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return userDomain.userInfo();
            }
        });
    }

}
