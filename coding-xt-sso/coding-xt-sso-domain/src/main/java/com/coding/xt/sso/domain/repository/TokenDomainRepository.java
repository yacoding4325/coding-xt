package com.coding.xt.sso.domain.repository;

import com.coding.xt.sso.domain.TokenDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 10:36
 */
@Component
public class TokenDomainRepository {

    @Autowired
    public StringRedisTemplate redisTemplate;

    public TokenDomain createDomain() {
        return new TokenDomain(this);
    }
}
