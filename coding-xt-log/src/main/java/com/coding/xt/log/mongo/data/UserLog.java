package com.coding.xt.log.mongo.data;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author yaCoding
 * @create 2022-09-30 下午 2:30
 */

@Data
@Document(collection = "user_log")
public class UserLog {

    private ObjectId id;

    private Long userId;

    private boolean newer;

    private Long registerTime;

    private Long lastLoginTime;

    private Integer sex;

}
