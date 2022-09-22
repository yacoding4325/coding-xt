package com.coding.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.admin.dao.TopicMapper;
import com.coding.xt.admin.domain.SubjectDomain;
import com.coding.xt.admin.domain.TopicDomain;
import com.coding.xt.admin.params.SubjectParam;
import com.coding.xt.admin.params.TopicParam;
import com.coding.xt.pojo.Topic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-09-22 下午 5:59
 */
//主题域存储库
@Component
public class TopicDomainRepository {

    @Resource
    private TopicMapper topicMapper;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    public TopicDomain createDomain(TopicParam topicParam) {
        return new TopicDomain(topicParam,this);
    }

    public Page<Topic> findPage(int currentPage, int size, String topicTitle, Long subjectId) {
        Page<Topic> page = new Page<>(currentPage,size);
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(topicTitle)) {
            queryWrapper.like(Topic::getTopicTitle, topicTitle);
        }
        if (subjectId != null){
            queryWrapper.eq(Topic::getTopicSubject,subjectId);
        }
        return topicMapper.selectPage(page, queryWrapper);
    }

    public SubjectDomain createSubjectDomain(SubjectParam subjectParam) {
        return subjectDomainRepository.createDomain(subjectParam);
    }

    public Topic findTopicByTitle(String topicTitle) {
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Topic::getTopicTitle,topicTitle);
        queryWrapper.last("limit 1");
        return topicMapper.selectOne(queryWrapper);
    }

    public void update(Topic topic) {
        topicMapper.updateById(topic);
    }

    public void save(Topic topic) {
        topicMapper.insert(topic);
    }

}
