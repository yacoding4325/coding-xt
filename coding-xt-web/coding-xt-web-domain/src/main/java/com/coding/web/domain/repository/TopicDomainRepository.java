package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.coding.web.domain.*;
import com.coding.xt.pojo.Topic;
import com.coding.xt.pojo.UserPractice;
import com.coding.xt.web.dao.TopicDTO;
import com.coding.xt.web.dao.TopicMapper;
import com.coding.xt.web.model.params.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:07
 */
@Component
public class TopicDomainRepository {

    @Resource
    private TopicMapper topicMapper;

    @Autowired
    private CourseDomainRepository courseDomainRepository;

    @Autowired
    private UserCourseDomainRepository userCourseDomainRepository;

    @Autowired
    private UserHistoryDomainRepository userHistoryDomainRepository;

    @Autowired
    private UserPracticeDomainRepository userPracticeDomainRepository;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    public TopicDomain createDomain(TopicParam topicParam) {
        return new TopicDomain(this,topicParam);
    }

    public CourseDomain getCourseDomain(CourseParam courseParam) {
        return courseDomainRepository.createDomain(courseParam);
    }

    public UserCourseDomain getUserCourseDomain(UserCourseParam userCourseParam) {
        return userCourseDomainRepository.createDomain(userCourseParam);
    }

    public UserHistoryDomain createHistoryDomain(UserHistoryParam userHistoryParam) {
        return userHistoryDomainRepository.createDomain(userHistoryParam);
    }

    public List<Long> findTopicRandomList(Long subjectId,
                                          String topicAreaPro,
                                          List<Integer> subjectUnitList) {

        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Topic::getTopicSubject,subjectId);
        if (StringUtils.isNotBlank(topicAreaPro)) {
            queryWrapper.likeRight(Topic::getTopicAreaPro, topicAreaPro);
        }
        if (CollectionUtils.isNotEmpty(subjectUnitList)) {
            queryWrapper.in(Topic::getSubjectUnit, subjectUnitList);
        }
        queryWrapper.last("order by RAND() LIMIT 50");
        Object object = topicMapper.selectObjs(queryWrapper);
        return (List<Long>) object;
    }

    public UserPracticeDomain createUserPracticeDomain(UserPracticeParam userPracticeParam) {
        return userPracticeDomainRepository.createDomain(userPracticeParam);
    }

    public TopicDTO findTopicAnswer(Long userId,Long topicId, Long userHistoryId) {
        UserPractice userPractice = this.userPracticeDomainRepository.createDomain(null).findUserPracticeByTopicId(userId,topicId,userHistoryId);
        Topic topic = topicMapper.selectById(topicId);
        TopicDTO topicDTO = new TopicDTO();
        BeanUtils.copyProperties(topic,topicDTO);
        topicDTO.setPStatus(userPractice.getPStatus());
        topicDTO.setUserAnswer(userPractice.getUserAnswer());
        return topicDTO;
    }

    public SubjectDomain createSubjectDomain(SubjectParam subjectParam) {
        return subjectDomainRepository.createDomain(subjectParam);
    }

    public TopicDTO findTopicAnswer(Long topicId, Long id) {
        return null;
    }

    public Topic findTopicById(Long topicId) {
        return null;
    }

    public UserHistoryDomain createUserHistoryDomain(Object o) {
        return null;
    }

    public UserProblemDomain createUserProblem(Object o) {
        return null;
    }
}
