package com.coding.xt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:03
 */
//科目模块的制作
//数据库的一些实体类
@Data
public class SubjectUnit {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long subjectId;
    private Integer subjectUnit;

}
