package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coding.web.domain.UserHistoryDomain;
import com.coding.xt.pojo.UserHistory;
import com.coding.xt.web.dao.UserCourseMapper;
import com.coding.xt.web.dao.UserHistoryMapper;
import com.coding.xt.web.model.SubjectModel;
import com.coding.xt.web.model.params.UserHistoryParam;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
        return userHistoryMapper.selectOne(queryWrapper);
    }

    public UserHistory findUserHistoryById(Long id) {
        return userHistoryMapper.selectById(id);
    }

    public void save(UserHistory userHistory) {
        userHistoryMapper.insert(userHistory);
    }

    public void updateUserHistoryStatus(Long historyId, int historyStatus, long finishTime) {
        UserHistory userHistory = new UserHistory();
        userHistory.setId(historyId);
        userHistory.setHistoryStatus(historyStatus);
        userHistory.setFinishTime(finishTime);
        userHistoryMapper.updateById(userHistory);
    }

    public void updateUserHistoryProgress(Long userHistoryId) {
        UpdateWrapper<UserHistory> update = Wrappers.update();
        update.eq("id",userHistoryId);
        update.set("progress","progress+1");
        this.userHistoryMapper.update(null, update);
    }

    public Integer countUserHistoryBySubjectList(Long userId, List<SubjectModel> subjectInfoByCourseId) {
        //计算 课程的学习次数 课程所包含的 学科 学习的次数 ，练习的次数
        List<Long> subjectIdList = subjectInfoByCourseId.stream().map(SubjectModel::getId).collect(Collectors.toList());
        LambdaQueryWrapper<UserHistory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserHistory::getUserId,userId);
        queryWrapper.in(UserHistory::getSubjectId,subjectIdList);
        return this.userHistoryMapper.selectCount(queryWrapper);
    }
}
