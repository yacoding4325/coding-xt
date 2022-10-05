package com.coding.xt.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yaCoding
 * @create 2022-10-05 上午 8:41
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillModel {

    private Long id;

    private String name;

    private String billDesc;

    private String billType;

}
