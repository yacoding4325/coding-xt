package com.coding.xt.web.model.params;

import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 3:44
 */
@Data
public class BillParam {

    private Integer page = 1;
    private Integer pageSize = 10;

    private Long id;
    private String name;
    private String billDesc;
    private String billType;
    /**
     * 0 正常 1 删除
     */
    private Integer status;

    private Long userId;

}
