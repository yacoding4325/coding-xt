package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coding.web.domain.UserHistoryDomain;
import com.coding.xt.pojo.UserHistory;
import com.coding.xt.web.dao.UserCourseMapper;
import com.coding.xt.web.dao.UserHistoryMapper;
import com.coding.xt.web.model.params.UserHistoryParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:29
 */
@Component
public class UserHistoryDomainRepository {

    @Resource
    private UserHistoryMapper userHistoryMapper;

    public UserHistoryDomain createDomain(UserHistoryParam userHistoryParam) {
        return new UserHistoryDomain(this,userHistoryParam);
    }

    public UserHistory findUserHistory(Long userId, Long subjectId, int historyStatus) {
        LambdaQueryWrapper<UserHistory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserHistory::getUserId,userId);
        queryWrapper.eq(UserHistory::getSubjectId,subjectId);
        queryWrapper.eq(UserHistory::getHistoryStatus,historyStatus);
        queryWrapper.last("limit 1");
        return userHistoryMapper.selectById(queryWrapper);
    }

    public UserHistory findUserHistoryById(Long userId, Long practiceId) {
        LambdaQueryWrapper<UserHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserHistory::getUserId,userId);
        queryWrapper.eq(UserHistory::getId,practiceId);
        return userHistoryMapper.selectOne(queryWrapper);
    }

    public void save(UserHistory userHistory) {
        this.userHistoryMapper.insert(userHistory);
    }
}
