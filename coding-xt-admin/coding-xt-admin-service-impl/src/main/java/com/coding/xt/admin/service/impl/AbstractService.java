package com.coding.xt.admin.service.impl;

import com.coding.xt.common.service.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {

    @Autowired
    protected ServiceTemplate serviceTemplate;
}
