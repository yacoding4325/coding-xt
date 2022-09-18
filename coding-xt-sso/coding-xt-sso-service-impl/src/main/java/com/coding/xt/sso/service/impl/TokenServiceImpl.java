package com.coding.xt.sso.service.impl;

import com.coding.xt.sso.domain.TokenDomain;
import com.coding.xt.sso.domain.repository.TokenDomainRepository;
import com.coding.xt.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yaCoding
 * @create 2022-09-18 下午 3:23
 */
@Service
public class TokenServiceImpl extends AbstractService implements TokenService {

    @Autowired
    private TokenDomainRepository tokenDomainRepository;

    @Override
    public Long checkToken(String token) {
        TokenDomain tokenDomain = tokenDomainRepository.createDomain();
        return tokenDomain.checkToken(token);
    }
}
