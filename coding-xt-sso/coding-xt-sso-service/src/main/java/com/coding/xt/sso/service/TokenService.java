package com.coding.xt.sso.service;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 9:44
 */

public interface TokenService {

    /**
     * token认证
     * @param token
     * @return
     */
    Long checkToken(String token);

}
