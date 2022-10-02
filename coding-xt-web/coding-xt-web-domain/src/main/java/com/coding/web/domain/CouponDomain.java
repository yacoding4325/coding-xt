package com.coding.web.domain;

import com.coding.web.domain.repository.CouponDomainRepository;
import com.coding.xt.pojo.Coupon;
import com.coding.xt.pojo.UserCoupon;
import com.coding.xt.web.model.params.CouponParam;

import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-10-02 上午 10:14
 */

public class CouponDomain {

    private CouponDomainRepository couponDomainRepository;

    private CouponParam couponParam;

    public CouponDomain(CouponDomainRepository couponDomainRepository, CouponParam couponParam) {
        this.couponDomainRepository = couponDomainRepository;
        this.couponParam = couponParam;
    }

    public List<UserCoupon> findUserCouponByUserId(Long userId) {
        return couponDomainRepository.findUserCouponByUserId(userId);
    }

    public Coupon findCouponById(Long couponId) {
        return couponDomainRepository.findCouponById(couponId);
    }

    public void updateCouponStatus(UserCoupon userCoupon) {
        couponDomainRepository.updateCouponStatus(userCoupon);
    }

    public void updateCouponNoUseStatus(Long userId, Long couponId, int frontStatusCode) {
        couponDomainRepository.updateCouponNoUseStatus(userId,couponId,frontStatusCode);
    }

}
