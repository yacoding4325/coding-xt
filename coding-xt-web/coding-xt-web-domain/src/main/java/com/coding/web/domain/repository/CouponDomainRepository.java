package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coding.web.domain.CouponDomain;
import com.coding.xt.pojo.Coupon;
import com.coding.xt.pojo.UserCoupon;
import com.coding.xt.web.dao.CouponMapper;
import com.coding.xt.web.dao.UserCouponMapper;
import com.coding.xt.web.model.params.CouponParam;
import org.springframework.stereotype.Component;
import sun.awt.LightweightFrame;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-10-02 上午 10:16
 */
@Component
public class CouponDomainRepository {

    @Resource
    private UserCouponMapper userCouponMapper;

    @Resource
    private CouponMapper couponMapper;

    public CouponDomain createDomain(CouponParam couponParam) {
        return new CouponDomain(this,couponParam);
    }

    public List<UserCoupon> findUserCouponByUserId(Long userId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper  = Wrappers.lambdaQuery();
        queryWrapper.eq(UserCoupon::getUserId,userId);
        queryWrapper.eq(UserCoupon::getStatus,0);
        List<UserCoupon> userCouponList = userCouponMapper.selectList(queryWrapper);
        return userCouponList;
    }

    public Coupon findCouponById(Long couponId) {
        return this.couponMapper.selectById(couponId);
    }

    public UserCoupon findUserCouponByUserId(Long userId, Long couponId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserCoupon::getUserId,userId);
        queryWrapper.eq(UserCoupon::getCouponId,couponId);
        queryWrapper.eq(UserCoupon::getStatus,0);
        queryWrapper.last("limit 1");
        UserCoupon userCoupon = userCouponMapper.selectOne(queryWrapper);
        return userCoupon;
    }

    public void updateCouponStatus(UserCoupon userCoupon) {
        LambdaUpdateWrapper<UserCoupon> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserCoupon::getId,userCoupon.getId());
        updateWrapper.set(UserCoupon::getStatus,userCoupon.getStatus());
        this.userCouponMapper.update(null, updateWrapper);
    }

    public void updateCouponNoUseStatus(Long userId, Long couponId, int frontStatusCode) {
        LambdaUpdateWrapper<UserCoupon> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserCoupon::getCouponId,couponId);
        updateWrapper.eq(UserCoupon::getUserId,userId);
        updateWrapper.eq(UserCoupon::getStatus,frontStatusCode);
        updateWrapper.set(UserCoupon::getStatus,0);
        this.userCouponMapper.update(null, updateWrapper);
    }

}
