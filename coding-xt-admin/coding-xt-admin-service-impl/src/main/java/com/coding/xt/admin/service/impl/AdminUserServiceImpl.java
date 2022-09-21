package com.coding.xt.admin.service.impl;

import com.coding.xt.admin.domain.AdminUserDomain;
import com.coding.xt.admin.domain.repository.AdminUserDomainRepository;
import com.coding.xt.admin.model.AdminUserModel;
import com.coding.xt.admin.params.AdminUserParam;
import com.coding.xt.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;

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
}
