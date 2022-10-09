package com.coding.web.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.web.domain.repository.UserProblemDomainRepository;
import com.coding.xt.pojo.UserProblem;
import com.coding.xt.web.model.params.UserProblemParam;

/**
 * @Author yaCoding
 * @create 2022-09-28 上午 11:31
 */

public class UserProblemDomain {

    private UserProblemDomainRepository userProblemDomainRepository;
    private UserProblemParam userProblemParam;

    public UserProblemDomain(UserProblemDomainRepository userProblemDomainRepository, UserProblemParam userProblemParam) {
        this.userProblemDomainRepository = userProblemDomainRepository;
        this.userProblemParam = userProblemParam;
    }

    public UserProblem getUserProblem(Long userId, Long topicId) {
        return userProblemDomainRepository.getUserProblem(userId,topicId);
    }

    public void saveUserProblem(UserProblem userProblem) {
        userProblemDomainRepository.save(userProblem);
    }

    public void updateUserProblemErrorCount(Long userId, Long topicId, String answer) {
        userProblemDomainRepository.updateUserProblemErrorCount(userId,topicId,answer);
    }

    public Page<UserProblem> findUserProblemList(Long userId, int errorStatus, int page, int pageSize) {
        return userProblemDomainRepository.findUserProblemList(userId,errorStatus,page,pageSize);
    }

    public Page<UserProblem> findUserProblemListBySubjectId(Long searchSubjectId, Long userId, int errorStatus, int page, int pageSize) {
        return userProblemDomainRepository.findUserProblemListBySubjectId(searchSubjectId,userId,errorStatus,page,pageSize);
    }

}
