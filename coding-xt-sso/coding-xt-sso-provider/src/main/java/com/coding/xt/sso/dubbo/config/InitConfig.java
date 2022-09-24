package com.coding.xt.sso.dubbo.config;

import com.coding.xt.sso.domain.repository.TokenDomainRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author yaCoding
 * @create 2022-09-24 下午 8:06
 */
//添加扫包路劲
@Configuration
@ComponentScan({"com.coding.xt.common.service"})
@Import(TokenDomainRepository.class)
public class InitConfig {
}
