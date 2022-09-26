package com.coding.web.domain;

import com.coding.web.domain.repository.UserHistoryDomainRepository;
import com.coding.xt.pojo.UserHistory;
import com.coding.xt.web.model.params.UserHistoryParam;

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
        return this.userHistoryDomainRepository.findUserHistory(userId,subjectId,historyStatus);
    }

    public UserHistory findUserHistoryById(Long userId, Long practiceId) {
        return this.userHistoryDomainRepository.findUserHistoryById(userId,practiceId);
    }

    public void saveUserHistory(UserHistory userHistory) {
        userHistoryDomainRepository.save(userHistory);
    }
}
