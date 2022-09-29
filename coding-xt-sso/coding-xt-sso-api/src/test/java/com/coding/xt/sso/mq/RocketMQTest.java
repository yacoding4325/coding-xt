package com.coding.xt.sso.mq;

import com.coding.xt.sso.dao.mongo.data.UserLog;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author yaCoding
 * @create 2022-09-29 下午 6:38
 */
@SpringBootTest
public class RocketMQTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testSend(){
        UserLog userLog = new UserLog();
        userLog.setNewer(true);
        userLog.setSex(1);
        userLog.setUserId(1000L);
        userLog.setLastLoginTime(System.currentTimeMillis());
        userLog.setRegisterTime(System.currentTimeMillis());
        rocketMQTemplate.convertAndSend("xt_log_sso_login",userLog);
    }

}
