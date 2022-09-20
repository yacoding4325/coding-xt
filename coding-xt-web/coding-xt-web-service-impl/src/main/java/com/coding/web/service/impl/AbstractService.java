package com.coding.web.service.impl;

import com.coding.xt.common.service.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 9:55
 */
//抽象类（服务模板）
public abstract class AbstractService {

    @Autowired
    protected ServiceTemplate serviceTemplate;

}
