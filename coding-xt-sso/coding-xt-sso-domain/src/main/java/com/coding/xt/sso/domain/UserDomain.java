package com.coding.xt.sso.domain;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 10:43
 */

import com.coding.xt.common.login.UserThreadLocal;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.sso.dao.data.User;
import com.coding.xt.sso.domain.repository.UserDomainRepository;
import com.coding.xt.sso.model.UserModel;
import com.coding.xt.sso.model.params.UserParam;
import org.springframework.beans.BeanUtils;

/**
 * 用户领域，权责明确，用户的事情应该教给用户的domain来去实现
 */
public class UserDomain {

    private UserDomainRepository userDomainRepository;
    private UserParam userParam;

    public UserDomain(UserDomainRepository userDomainRepository, UserParam userParam) {
        this.userDomainRepository = userDomainRepository;
        this.userParam = userParam;
    }

    public void updateUser(User user) {
        userDomainRepository.updateUser(user);
    }

    public void saveUser(User user) {
        userDomainRepository.saveUser(user);
    }

    public User findUserByUnionId(String unionId) {
        return userDomainRepository.findUserByUnionId(unionId);
    }

    public CallResult<Object> userInfo() {
        Long userId = UserThreadLocal.get();
        //这个地方 可以考虑做一个缓存，将用户的信息临时存储起来，用于在访问过程中的user信息的提取，后续做优化
        User user = userDomainRepository.findUserById(userId);

        //注意，返回的信息 不能是User user只是数据库表字段的映射实体，和业务中使用的User是不一样的
        //view 需要有自己的实体对象 来去映射页面的显示
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user,userModel);
        return CallResult.success(userModel);
    }
}

