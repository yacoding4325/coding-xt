package com.coding.xt.sso.service.impl;

import com.coding.xt.common.service.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 9:55
 */

public abstract class AbstractService {

    @Autowired
    private ServiceTemplate serviceTemplate;

}
