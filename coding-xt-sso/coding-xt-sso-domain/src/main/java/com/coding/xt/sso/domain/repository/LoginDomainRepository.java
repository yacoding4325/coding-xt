package com.coding.xt.sso.domain.repository;

import com.coding.xt.common.constants.RedisKey;
import com.coding.xt.common.wx.config.WxOpenConfig;
import com.coding.xt.sso.domain.InviteDomain;
import com.coding.xt.sso.domain.LoginDomain;
import com.coding.xt.sso.domain.UserDomain;
import com.coding.xt.sso.model.params.InviteParam;
import com.coding.xt.sso.model.params.LoginParam;
import com.coding.xt.sso.model.params.UserParam;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 10:01
 */

@Component
public class LoginDomainRepository {

    @Autowired
    public WxMpService wxMpService;
    @Autowired
    public WxMpService wxMpServiceGzh;
    @Autowired
    private WxOpenConfig wxOpenConfig;
    @Autowired
    public StringRedisTemplate redisTemplate;
    @Autowired
    private UserDomainRepository userDomainRepository;


    public LoginDomain createDomain(LoginParam loginParam) {
        return new LoginDomain(this,loginParam);
    }

    public boolean checkState(String state) {
        Boolean isValid = redisTemplate.hasKey(RedisKey.WX_STATE_KEY+state);
        return isValid != null && isValid;
    }

    public String buildQrUrl() {
        //        String csrfKey = wxOpenConfig.getCsrfKey();
//        String time = new DateTime().toString("yyyyMMddHHmmss");
//        csrfKey = csrfKey+"_"+time;
        String csrfKey = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(RedisKey.WX_STATE_KEY+csrfKey,"1",60, TimeUnit.SECONDS);
        //csrf 跨站伪造攻击 http://xxxx/sso/login/wxLoginCallBack？state=csrfKey 需要验证csrfKey是不是我们存储的，
        // 如果是就证明 此链接安全 不是伪造的
        //典型的场景 比如你登录了银行网站 这时候 你去访问了一个论坛，点击论坛里面的一个图片 ，但是你的账户少钱了
        //将csrfKey放入redis当中，并设置有效期
        //会用到一个第三方的工具
        String url = wxMpService.buildQrConnectUrl(wxOpenConfig.getRedirectUrl(), wxOpenConfig.getScope(), csrfKey);
        return url;
    }
    public String buildGzhUrl() {
        String csrfKey = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(RedisKey.WX_STATE_KEY+csrfKey,"1",60, TimeUnit.SECONDS);
        String url = wxMpServiceGzh.oauth2buildAuthorizationUrl(wxOpenConfig.mobileRedirectUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, csrfKey);
        return url;
    }


    public UserDomain createUserDomain(UserParam userParam) {
        return userDomainRepository.createDomain(userParam);
    }

    @Autowired
    private InviteDomainRepository inviteDomainRepository;

    public InviteDomain createInviteDomain(InviteParam inviteParam) {

        return inviteDomainRepository.createDomain(inviteParam);
    }


}
