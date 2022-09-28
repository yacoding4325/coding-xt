package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coding.web.domain.UserProblemDomain;
import com.coding.xt.pojo.UserProblem;
import com.coding.xt.web.dao.UserProblemMapper;
import com.coding.xt.web.model.params.UserProblemParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-09-28 上午 11:32
 */
@Component
public class UserProblemDomainRepository {

    @Resource
    private UserProblemMapper userProblemMapper;

    public UserProblemDomain createDomain(UserProblemParam userProblemParam) {
        return new UserProblemDomain(this,userProblemParam);
    }

    public UserProblem getUserProblem(Long userId, Long topicId) {
        LambdaQueryWrapper<UserProblem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserProblem::getTopicId,topicId);
        queryWrapper.eq(UserProblem::getUserId, userId);
        queryWrapper.last("limit 1");
        return userProblemMapper.selectOne(queryWrapper);
    }

    public void save(UserProblem userProblem) {
        this.userProblemMapper.insert(userProblem);
    }

    public void updateUserProblemErrorCount(Long userId, Long topicId, String answer) {
        UpdateWrapper<UserProblem> update = Wrappers.update();
        update.eq("user_id",userId);
        update.eq("topic_id",topicId);
        update.set("error_count","error_count+1");
        update.set("answer",answer);
        userProblemMapper.update(null, update);
    }
}
