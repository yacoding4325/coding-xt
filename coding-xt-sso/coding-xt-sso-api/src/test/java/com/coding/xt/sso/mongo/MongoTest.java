package com.coding.xt.sso.mongo;

import com.coding.xt.sso.dao.mongo.data.UserLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @Author yaCoding
 * @create 2022-09-29 下午 6:35
 */

@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSave(){
        UserLog userLog = new UserLog();
        userLog.setNewer(true);
        userLog.setSex(1);
        userLog.setUserId(1000L);
        userLog.setLastLoginTime(System.currentTimeMillis());
        userLog.setRegisterTime(System.currentTimeMillis());
        mongoTemplate.save(userLog);

        Query query = Query.query(Criteria.where("userId").is(1000));
        query.limit(1);
        UserLog log = mongoTemplate.findOne(query, UserLog.class);
        System.out.println(log);
    }

}
