package com.coding.xt.web.service;

import com.coding.xt.common.model.CallResult;

/**
 * @Author yaCoding
 * @create 2022-09-25 上午 10:59
 */

public interface SubjectService {

    /**
     * 查询所有的科目信息 去重
     * @return
     */
    CallResult listSubject();

}
