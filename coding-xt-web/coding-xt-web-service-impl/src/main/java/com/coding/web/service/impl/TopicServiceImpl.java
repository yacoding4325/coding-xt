package com.coding.web.service.impl;

import com.coding.web.domain.TopicDomain;
import com.coding.web.domain.repository.TopicDomainRepository;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import com.coding.xt.web.model.params.TopicParam;
import com.coding.xt.web.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:06
 */

@Service
public class TopicServiceImpl extends AbstractService implements TopicService {

    @Autowired
    private TopicDomainRepository topicDomainRepository;

    @Override
    @Transactional
    public CallResult practice(TopicParam topicParam) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParam);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> checkBiz() {
                return topicDomain.checkPracticeBiz();
            }

            @Override
            public CallResult<Object> doAction() {
                return topicDomain.practice();
            }
        });
    }

    //实现类--提交功能
    @Override
    @Transactional
    public CallResult submit(TopicParam topicParam) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParam);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> checkBiz() {
                return topicDomain.checkSubmitBiz();
            }

            @Override
            public CallResult<Object> doAction() {
                return topicDomain.submit();
            }
        });
    }

    @Override
    public CallResult jump(TopicParam topicParam) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.jump();
            }
        });
    }
}
