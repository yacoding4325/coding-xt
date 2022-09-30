package com.coding.xt.sso.domain.thread;

import com.coding.xt.sso.dao.mongo.data.UserLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author yaCoding
 * @create 2022-09-29 下午 8:58
 */

/**
 * 使用线程池，将队列发消息的行为放入线程池执行：
 */
@Component
@Slf4j
public class LogThread {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Async("taskExecutor")
    public void recordLog(UserLog userLog) {
        try {
            rocketMQTemplate.convertAndSend("xt_log_sso_login",userLog);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("record-log:{}",userLog);
        }
        log.info("记录日志完成时间:{}",new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
    }

}
