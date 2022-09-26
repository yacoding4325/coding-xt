package com.coding.xt.web.service;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.TopicParam;
/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:06
 */

public interface TopicService {

    /**
     * 开始学习
     * @param topicParam
     * @return
     */
    CallResult practice(TopicParam topicParam);
}
