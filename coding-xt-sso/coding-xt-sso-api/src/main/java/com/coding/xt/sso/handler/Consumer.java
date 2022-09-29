package com.coding.xt.sso.handler;

import com.coding.xt.sso.dao.mongo.data.UserLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Author yaCoding
 * @create 2022-09-29 下午 1:40
 */

@RocketMQMessageListener(topic = "xt_log_sso_login",consumerGroup = "login_group")
@Slf4j
@Component
public class Consumer implements RocketMQListener<UserLog> {

    @Override
    public void onMessage(UserLog message) {
        log.info("消息消费:{}",message);
    }

}
