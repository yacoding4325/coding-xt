package com.coding.web.domain;

import com.coding.web.domain.repository.UserPracticeDomainRepository;
import com.coding.xt.pojo.UserPractice;
import com.coding.xt.web.model.params.UserPracticeParam;

import java.util.List;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 7:36
 */

public class UserPracticeDomain {

    private UserPracticeDomainRepository userPracticeDomainRepository;
    private UserPracticeParam userPracticeParam;

    public UserPracticeDomain(UserPracticeDomainRepository userPracticeDomainRepository, UserPracticeParam userPracticeParam) {
        this.userPracticeDomainRepository = userPracticeDomainRepository;
        this.userPracticeParam = userPracticeParam;
    }

    public void saveUserPractice(UserPractice userPractice) {
        userPracticeDomainRepository.save(userPractice);
    }

    public int countUserPracticeTrueNum(Long userId,Long userHistoryId) {
        return userPracticeDomainRepository.countUserPracticeNumByStatus(userId,userHistoryId,2);

    }

    public int countUserPracticeWrongNum(Long userId,Long userHistoryId) {
        //1 错误答案
        return userPracticeDomainRepository.countUserPracticeNumByStatus(userId,userHistoryId,1);
    }

    public List<Map<String, Object>> findUserPracticeAll(Long userId, Long userHistoryId) {
        return userPracticeDomainRepository.findUserPracticeAnswerMap(userId,userHistoryId);
    }

    public Long findUserPractice(Long userId, Long userHistoryId, Integer progress) {
        return userPracticeDomainRepository.findUserPracticeTopic(userId,userHistoryId,progress);
    }

    public UserPractice findUserPracticeByTopicId(Long userId, Long topicId, Long userHistoryId) {
        return userPracticeDomainRepository.findUserPracticeByTopicId(userId, topicId, userHistoryId);
    }

    public int countUserPracticeTrueNum(Long id) {
        return 0;
    }

    public int countUserPracticeWrongNum(Long id) {
        return 0;
    }

    public void updateUserPractice(Long userHistoryId, Long topicId, Long userId, String answer, int pStatus) {
        userPracticeDomainRepository.updateUserPractice(userHistoryId,topicId,userId,answer,pStatus);
    }
}
