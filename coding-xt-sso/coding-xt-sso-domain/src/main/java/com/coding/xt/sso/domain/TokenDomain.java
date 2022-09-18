package com.coding.xt.sso.domain;

import org.apache.commons.lang3.StringUtils;

import com.coding.xt.common.constants.RedisKey;
import com.coding.xt.common.utils.JwtUtil;
import com.coding.xt.sso.domain.repository.TokenDomainRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 10:38
 */
@Slf4j
public class TokenDomain {

    private TokenDomainRepository tokenDomainRepository;

    public TokenDomain(TokenDomainRepository tokenDomainRepository) {
        this.tokenDomainRepository = tokenDomainRepository;
    }

    public Long checkToken(String token) {
        /**
         * 1. 检测token字符串是否合法
         * 2. 检测redis是否有此token
         */
        try {
            JwtUtil.parseJWT(token,LoginDomain.secretKey);
            String userIdStr = tokenDomainRepository.redisTemplate.opsForValue().get(RedisKey.TOKEN + token);
            if (StringUtils.isBlank(userIdStr)){
                return null;
            }
            return Long.parseLong(userIdStr);
        }catch (Exception e){
            e.printStackTrace();
            log.error("checkToken error:{}",e.getMessage(),e);
            return null;
        }
    }
}
