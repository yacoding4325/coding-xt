package com.coding.xt.sso.domain;

import com.coding.xt.common.constants.RedisKey;
import com.coding.xt.common.enums.InviteType;
import com.coding.xt.common.model.BusinessCodeEnum;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.utils.AESUtils;
import com.coding.xt.common.utils.JwtUtil;
import com.coding.xt.sso.dao.data.User;
import com.coding.xt.sso.domain.repository.LoginDomainRepository;
import com.coding.xt.sso.model.enums.LoginType;
import com.coding.xt.sso.model.params.LoginParam;
import com.coding.xt.sso.model.params.UserParam;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author yaCoding
 * @create 2022-09-17 下午 10:02
 */
/**
 * 专门处理和登录相关的操作
 */

public class LoginDomain {

    private LoginDomainRepository loginDomainRepository;

    private LoginParam loginParam;

    public static final String secretKey = "mszlu!@#$%xtsso&^#$#@@";

    public LoginDomain(LoginDomainRepository loginDomainRepository, LoginParam loginParam) {
        this.loginDomainRepository = loginDomainRepository;
        this.loginParam = loginParam;
    }

    public CallResult<Object> buildQrConnectUrl() {
        String url = this.loginDomainRepository.buildQrUrl();
        return CallResult.success(url);
    }

    public CallResult<Object> checkWxLoginCallBackBiz() {
        //主要检查 state是否是合法的
        //csrf的检测
        String state = loginParam.getState();
        //去redis检测 是否 state 为key的值存在，如果不存在 ，证明不合法
        boolean isVerify = loginDomainRepository.checkState(state);
        if (!isVerify){
            return CallResult.fail(BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getCode(),"参数不合法");
        }
        return CallResult.success();
    }

    public CallResult<Object> wxLoginCallBack() {
        String code = loginParam.getCode();
        try {
            //2. 下次进行登录的时候，如果refreshToken存在，可以直接获取accessToken，不需要用户重新授权
            String refreshToken = loginDomainRepository.redisTemplate.opsForValue().get(RedisKey.REFRESH_TOKEN);
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
            if (refreshToken == null) {
                //1. 通过code获取到accessToken和refreshToken ，
                wxMpOAuth2AccessToken = loginDomainRepository.wxMpService.oauth2getAccessToken(code);
                refreshToken = wxMpOAuth2AccessToken.getRefreshToken();
                // 需要保存refreshToken 保存在redis当中，过期时间 设置为 28天
                loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.REFRESH_TOKEN, refreshToken, 28, TimeUnit.DAYS);
            }else{
                wxMpOAuth2AccessToken = loginDomainRepository.wxMpService.oauth2refreshAccessToken(refreshToken);
            }
            //3. 通过accessToken获取到了微信的用户信息（openid，unionId）unionId在web端，公众号端，手机端唯一的
            WxMpUser wxMpUser = loginDomainRepository.wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
            String unionId = wxMpUser.getUnionId();
            //4. 需要判断unionId在数据库中的user表中 是否存在 存在就更新最后登录时间 不存在 注册
            User user = this.loginDomainRepository.createUserDomain(new UserParam()).findUserByUnionId(unionId);
            boolean isNew = false;
            if (user == null){
                //注册
                user = new User();
                Long currentTime = System.currentTimeMillis();
                user.setNickname(wxMpUser.getNickname());
                user.setHeadImageUrl(wxMpUser.getHeadImgUrl());
                user.setSex(wxMpUser.getSex());
                user.setOpenid(wxMpUser.getOpenId());
                user.setLoginType(LoginType.WX.getCode());
                user.setCountry(wxMpUser.getCountry());
                user.setCity(wxMpUser.getCity());
                user.setProvince(wxMpUser.getProvince());
                user.setRegisterTime(currentTime);
                user.setLastLoginTime(currentTime);
                user.setUnionId(wxMpUser.getUnionId());
                user.setArea("");
                user.setMobile("");
                user.setGrade("");
                user.setName(wxMpUser.getNickname());
                user.setSchool("");
                this.loginDomainRepository.createUserDomain(new UserParam()).saveUser(user);
                isNew = true;
                //新用户 需要判断是否有邀请信息
                fillInvite(user);

            }

            //5. 使用jwt技术，生成token，需要把token存储起来
            String token = JwtUtil.createJWT(7 * 24 * 60 * 60 * 1000, user.getId(), secretKey);
            loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.TOKEN+token,String.valueOf(user.getId()),7,TimeUnit.DAYS);
            //6. 因为付费课程，所以账号只能在一端登录，如果用户在其他地方登录，需要将当前的登录用户踢下线
            String oldToken = loginDomainRepository.redisTemplate.opsForValue().get(RedisKey.LOGIN_USER_TOKEN + user.getId());
            if (oldToken != null){
                //当前用户 之前在某一个设备登录过
                //在用户进行登录验证的时候，需要先验证token是否合法，然后去redis查询是否存在token 不存在 代表不合法
                loginDomainRepository.redisTemplate.delete(RedisKey.TOKEN+token);
            }
            loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.LOGIN_USER_TOKEN + user.getId(),token);
            //7. 返回给前端token，存在cookie当中，下次请求的时候 从cookie中获取token
            HttpServletResponse response = loginParam.getResponse();
            Cookie cookie = new Cookie("t_token", token);
            cookie.setMaxAge(8*24*3600);
            cookie.setPath("/");
            response.addCookie(cookie);
            //8. 比如给用户 加积分，成就系统，任务系统
            //9. 需要记录日志，记录当前用户的登录行为  MQ+mongo进行日志记录
            //10.更新用户的最后登录时间
            if (!isNew){
                user.setLastLoginTime(System.currentTimeMillis());
                this.loginDomainRepository.createUserDomain(new UserParam()).updateUser(user);
            }
            return CallResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return CallResult.fail(BusinessCodeEnum.LOGIN_WX_NOT_USER_INFO.getCode(),"授权问题,无法获取用户信息");
        }
    }

    /**
     * 邀请信息
     * @param user
     */
    private void fillInvite(User user) {
        HttpServletRequest request = this.loginParam.getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return;
        }
        List<Map<String,String>> billTypeList = new ArrayList<>();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String[] inviteCookie = name.split("_i_ga_b_");
            if (inviteCookie.length == 2){
                Map<String,String> map = new HashMap<>();
                map.put("billType",inviteCookie[1]);
                map.put("userId",cookie.getValue());
                billTypeList.add(map);
            }
        }
    }


    public String buildGzhUrl() {
        String url = this.loginDomainRepository.buildGzhUrl();
        return url;
    }

    public CallResult<Object> wxGzhLoginCallBack() {
        String code = loginParam.getCode();
        try {
            //2. 下次进行登录的时候，如果refreshToken存在，可以直接获取accessToken，不需要用户重新授权
            String refreshToken = loginDomainRepository.redisTemplate.opsForValue().get(RedisKey.GZH_REFRESH_TOKEN);
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
            if (refreshToken == null) {
                //1. 通过code获取到accessToken和refreshToken ，
                wxMpOAuth2AccessToken = loginDomainRepository.wxMpServiceGzh.oauth2getAccessToken(code);
                refreshToken = wxMpOAuth2AccessToken.getRefreshToken();
                // 需要保存refreshToken 保存在redis当中，过期时间 设置为 28天
                loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.GZH_REFRESH_TOKEN, refreshToken, 28, TimeUnit.DAYS);
            }else{
                wxMpOAuth2AccessToken = loginDomainRepository.wxMpServiceGzh.oauth2refreshAccessToken(refreshToken);
            }
            //3. 通过accessToken获取到了微信的用户信息（openid，unionId）unionId在web端，公众号端，手机端唯一的
            WxMpUser wxMpUser = loginDomainRepository.wxMpServiceGzh.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
            //如果是正式账号 是没有此问题的，测试整合 unionId 不存在
            String unionId = wxMpUser.getUnionId();
            if (unionId == null){
                unionId = wxMpOAuth2AccessToken.getOpenId();
            }
            //4. 需要判断unionId在数据库中的user表中 是否存在 存在就更新最后登录时间 不存在 注册
            User user = this.loginDomainRepository.createUserDomain(new UserParam()).findUserByUnionId(unionId);
            boolean isNew = false;
            if (user == null){
                //注册
                user = new User();
                Long currentTime = System.currentTimeMillis();
                user.setNickname(wxMpUser.getNickname());
                user.setHeadImageUrl(wxMpUser.getHeadImgUrl());
                user.setSex(wxMpUser.getSex());
                user.setOpenid(wxMpUser.getOpenId());
                user.setLoginType(LoginType.WX.getCode());
                user.setCountry(wxMpUser.getCountry());
                user.setCity(wxMpUser.getCity());
                user.setProvince(wxMpUser.getProvince());
                user.setRegisterTime(currentTime);
                user.setLastLoginTime(currentTime);
                user.setUnionId(unionId);
                user.setArea("");
                user.setMobile("");
                user.setGrade("");
                user.setName(wxMpUser.getNickname());
                user.setSchool("");
                this.loginDomainRepository.createUserDomain(new UserParam()).saveUser(user);
                isNew = true;
            }

            //5. 使用jwt技术，生成token，需要把token存储起来
            String token = JwtUtil.createJWT(7 * 24 * 60 * 60 * 1000, user.getId(), secretKey);
            loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.TOKEN+token,String.valueOf(user.getId()),7,TimeUnit.DAYS);
            //6. 因为付费课程，所以账号只能在一端登录，如果用户在其他地方登录，需要将当前的登录用户踢下线
            String oldToken = loginDomainRepository.redisTemplate.opsForValue().get(RedisKey.LOGIN_USER_TOKEN + user.getId());
            if (oldToken != null){
                //当前用户 之前在某一个设备登录过
                //在用户进行登录验证的时候，需要先验证token是否合法，然后去redis查询是否存在token 不存在 代表不合法
                loginDomainRepository.redisTemplate.delete(RedisKey.TOKEN+token);
            }
            loginDomainRepository.redisTemplate.opsForValue().set(RedisKey.LOGIN_USER_TOKEN + user.getId(),token);
            //7. 返回给前端token，存在cookie当中，下次请求的时候 从cookie中获取token
            HttpServletResponse response = loginParam.getResponse();
            Cookie cookie = new Cookie("t_token", token);
            cookie.setMaxAge(8*24*3600);
            cookie.setPath("/");
            response.addCookie(cookie);
            //8. 比如给用户 加积分，成就系统，任务系统
            //9. 需要记录日志，记录当前用户的登录行为  MQ+mongo进行日志记录
            //10.更新用户的最后登录时间
            if (!isNew){
                user.setLastLoginTime(System.currentTimeMillis());
                this.loginDomainRepository.createUserDomain(new UserParam()).updateUser(user);
            }
            return CallResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return CallResult.fail(BusinessCodeEnum.LOGIN_WX_NOT_USER_INFO.getCode(),"授权问题,无法获取用户信息");
        }
    }
}
