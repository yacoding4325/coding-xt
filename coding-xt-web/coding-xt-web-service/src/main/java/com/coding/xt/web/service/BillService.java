package com.coding.xt.web.service;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.BillParam;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 3:43
 */

public interface BillService {
    /**
     * 查询所有的海报列表
     * @return
     */
    CallResult all(BillParam billParam);

    /**
     * 生成推广链接(海报)
     * @param billParam
     * @return
     */
    CallResult gen(BillParam billParam);
}
