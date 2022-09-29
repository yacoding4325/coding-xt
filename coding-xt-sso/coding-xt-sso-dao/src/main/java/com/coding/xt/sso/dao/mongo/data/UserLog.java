package com.coding.xt.sso.dao.mongo.data;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author yaCoding
 * @create 2022-09-29 上午 10:51
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
