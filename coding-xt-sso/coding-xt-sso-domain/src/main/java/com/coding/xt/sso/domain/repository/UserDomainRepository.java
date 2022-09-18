package com.coding.xt.sso.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.coding.xt.sso.dao.UserMapper;
import com.coding.xt.sso.dao.data.User;
import com.coding.xt.sso.domain.UserDomain;
import com.coding.xt.sso.model.params.UserParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 10:43
 */
@Component
public class UserDomainRepository {

    @Resource
    private UserMapper userMapper;

    public UserDomain createDomain(UserParam userParam) {
        return new UserDomain(this,userParam);
    }

    public User findUserByUnionId(String unionId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //limit 1是对应的一个优化，查到数据就不在检索了
        queryWrapper.eq(User::getUnionId,unionId).last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }

    public void saveUser(User user) {
        userMapper.insert(user);
    }

    public void updateUser(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getLastLoginTime,user.getLastLoginTime());
        updateWrapper.eq(User::getId,user.getId());
        userMapper.update(null, updateWrapper);
    }

    public User findUserById(Long userId) {
        return null;
    }
}
