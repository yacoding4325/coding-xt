package com.coding.web.domain.mq;

import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 10:39
 */

public interface MqService {

    /**
     *
     * @param create_order_delay
     * @param map
     * @param i
     */
    void sendDelayMessage(String create_order_delay, Map<String, String> map, int i);

}
