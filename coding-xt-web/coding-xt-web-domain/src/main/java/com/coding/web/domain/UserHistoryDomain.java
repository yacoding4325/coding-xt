package com.coding.web.domain;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coding.web.domain.repository.UserHistoryDomainRepository;
import com.coding.xt.pojo.UserHistory;
import com.coding.xt.web.dao.UserHistoryMapper;
import com.coding.xt.web.model.params.UserHistoryParam;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:28
 */

public class UserHistoryDomain {

    private UserHistoryDomainRepository userHistoryDomainRepository;
    private UserHistoryParam userHistoryParam;
    private UserHistoryMapper userHistoryMapper;

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

    public void updateUserHistoryErrorCount(Long userHistoryId) {
        UpdateWrapper<UserHistory> update = Wrappers.update();
        update.eq("id",userHistoryId);
        update.set("error_count","error_count+1");
        this.userHistoryMapper.update(null, update);
    }

    public void updateUserHistoryStatus(Long historyId, int historyStatus, long finishTime) {
        LambdaUpdateWrapper<UserHistory> update = Wrappers.lambdaUpdate();
        update.eq(UserHistory::getId,historyId);
        update.set(UserHistory::getHistoryStatus,historyStatus);
        update.set(UserHistory::getFinishTime,finishTime);
        this.userHistoryMapper.update(null, update);
    }

    public void updateUserHistoryProgress(Long historyId) {
        UpdateWrapper<UserHistory> update = Wrappers.update();
        update.eq("id",historyId);
        update.set("progress","progress+1");
        this.userHistoryMapper.update(null, update);
    }
}
