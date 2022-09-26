package com.coding.xt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.xt.pojo.Topic;
import org.apache.ibatis.annotations.Param;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 7:30
 */

public interface TopicMapper extends BaseMapper<Topic> {

    TopicDTO findTopicAnswer(@Param("topicId")Long topicId,
                             @Param("userHistoryId") Long userHistoryId);

}
