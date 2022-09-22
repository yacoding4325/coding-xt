package com.coding.xt.admin.service;

import com.coding.xt.admin.model.TopicModel;
import com.coding.xt.admin.params.TopicParam;
import com.coding.xt.common.model.CallResult;

/**
 * @Author yaCoding
 * @create 2022-09-22 下午 6:00
 */

public interface TopicService {

    /**
     * 根据条件 进行对应的题目 分页查询
     * @param topicParam
     * @return
     */
    CallResult findTopicList(TopicParam topicParam);

    TopicModel findTopicByTitle(String topicTitle);

    CallResult updateTopic(TopicParam topicParam);

    CallResult saveTopic(TopicParam topicParam);

}
