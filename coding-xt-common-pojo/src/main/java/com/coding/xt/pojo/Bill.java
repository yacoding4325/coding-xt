package com.coding.xt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

    private Long id;

    private String name;

    private String billDesc;

    private String billType;

    private Integer deleteStatus;

}