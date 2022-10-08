package com.coding.xt.admin.service.impl;

import com.coding.xt.admin.domain.AdminUserDomain;
import com.coding.xt.admin.domain.repository.AdminUserDomainRepository;
import com.coding.xt.admin.model.AdminUserModel;
import com.coding.xt.admin.params.AdminUserParam;
import com.coding.xt.admin.service.AdminUserService;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:18
 */

public class AdminUserServiceImpl extends AbstractService implements AdminUserService {

    @Autowired
    private AdminUserDomainRepository adminUserDomainRepository;

    @Override
    public AdminUserModel findUserByUsername(String username) {
        AdminUserParam adminUserParam = new AdminUserParam();
        adminUserParam.setUsername(username);
        AdminUserDomain adminUserDomain  = adminUserDomainRepository.createDomain(adminUserParam);
        return adminUserDomain.findUserByUsername();
    }

    //身份验证
    @Override
    public boolean auth(String requestURI, Long userId) {
        AdminUserDomain adminUserDomain  = adminUserDomainRepository.createDomain(new AdminUserParam());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Boolean>() {
            @Override
            public CallResult<Boolean> doAction() {
                return adminUserDomain.auth(requestURI,userId);
            }
        }).getResult();
    }

    @Override
    public CallResult findRolePage(AdminUserParam adminUserParam) {
        AdminUserDomain adminUserDomain  = adminUserDomainRepository.createDomain(adminUserParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.findRolePage();
            }
        });
    }

    @Override
    public CallResult permissionAll() {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(null);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.permissionAll();
            }
        });
    }

    @Override
    @Transactional
    public CallResult add(AdminUserParam adminUserParam) {
        AdminUserDomain adminUserDomain = this.adminUserDomainRepository.createDomain(adminUserParam);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return adminUserDomain.add();
            }
        });
    }

}
