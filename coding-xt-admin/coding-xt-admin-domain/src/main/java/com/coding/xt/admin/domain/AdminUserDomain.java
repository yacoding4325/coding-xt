package com.coding.xt.admin.domain;

import com.coding.xt.admin.dao.data.AdminUser;
import com.coding.xt.admin.domain.repository.AdminUserDomainRepository;
import com.coding.xt.admin.model.AdminUserModel;
import com.coding.xt.admin.model.params.AdminUserParam;
import org.springframework.beans.BeanUtils;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:21
 */

public class AdminUserDomain {

    private AdminUserDomainRepository adminUserDomainRepository;
    private AdminUserParam adminUserParam;

    public AdminUserDomain(AdminUserDomainRepository adminUserDomainRepository, AdminUserParam adminUserParam) {
        this.adminUserDomainRepository = adminUserDomainRepository;
        this.adminUserParam = adminUserParam;
    }

    public AdminUserModel findUserByUsername() {
        AdminUser adminUser = adminUserDomainRepository.findUserByUsername(this.adminUserParam.getUsername());
        AdminUserModel adminUserModel = new AdminUserModel();
        BeanUtils.copyProperties(adminUser,adminUserModel);
        return adminUserModel;
    }
}
