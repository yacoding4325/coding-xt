package com.coding.web.domain;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coding.web.domain.repository.UserHistoryDomainRepository;
import com.coding.xt.pojo.UserHistory;
import com.coding.xt.web.dao.UserHistoryMapper;
import com.coding.xt.web.model.SubjectModel;
import com.coding.xt.web.model.params.UserHistoryParam;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:28
 */

public class UserHistoryDomain {

    private UserHistoryDomainRepository userHistoryDomainRepository;

    private UserHistoryParam userHistoryParam;

    public UserHistoryDomain(UserHistoryDomainRepository userHistoryDomainRepository, UserHistoryParam userHistoryParam) {
        this.userHistoryDomainRepository = userHistoryDomainRepository;
        this.userHistoryParam = userHistoryParam;
    }

    public UserHistory findUserHistory(Long userId, Long subjectId, int historyStatus) {
        return userHistoryDomainRepository.findUserHistory(userId,subjectId,historyStatus);
    }

    public UserHistory findUserHistoryById(Long id) {
        return userHistoryDomainRepository.findUserHistoryById(id);
    }

    public void saveUserHistory(UserHistory userHistory) {
        userHistoryDomainRepository.save(userHistory);
    }

    public void updateUserHistoryStatus(Long historyId, int historyStatus, long finishTime) {
        userHistoryDomainRepository.updateUserHistoryStatus(historyId,historyStatus,finishTime);
    }

    public void updateUserHistoryProgress(Long userHistoryId) {
        userHistoryDomainRepository.updateUserHistoryProgress(userHistoryId);
    }

    public Integer countUserHistoryBySubjectList(Long userId, List<SubjectModel> subjectInfoByCourseId) {
        return userHistoryDomainRepository.countUserHistoryBySubjectList(userId,subjectInfoByCourseId);
    }
}
