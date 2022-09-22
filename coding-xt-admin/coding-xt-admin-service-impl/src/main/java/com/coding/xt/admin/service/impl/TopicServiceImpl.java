package com.coding.xt.admin.service.impl;

import com.coding.xt.admin.domain.TopicDomain;
import com.coding.xt.admin.domain.repository.TopicDomainRepository;
import com.coding.xt.admin.model.TopicModel;
import com.coding.xt.admin.params.TopicParam;
import com.coding.xt.admin.service.TopicService;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author yaCoding
 * @create 2022-09-22 下午 6:00
 */
//主题服务实现类
@Service
public class TopicServiceImpl extends AbstractService implements TopicService {

    @Autowired
    private TopicDomainRepository topicDomainRepository;

    //查找主题列表
    @Override
    public CallResult findTopicList(TopicParam topicParam) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParam);
        
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.findTopicList();
            }
        } );
    }

    //按标题查找主题
    @Override
    public TopicModel findTopicByTitle(String topicTitle) {
        TopicParam topicParam = new TopicParam();
        topicParam.setTopicTitle(topicTitle);
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParam);
        return topicDomain.findTopicByTitle();
    }

    //更新主题
    @Override
    @Transactional
    public CallResult updateTopic(TopicParam topicParam) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParam);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.updateTopic();
            }
        });
    }

    //主题保存
    @Override
    @Transactional
    public CallResult saveTopic(TopicParam topicParam) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParam);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.saveTopic();
            }
        });
    }

}
