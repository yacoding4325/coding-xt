package com.coding.xt.sso.service.impl;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import com.coding.xt.common.wx.config.WxOpenConfig;
import com.coding.xt.sso.domain.LoginDomain;
import com.coding.xt.sso.domain.repository.LoginDomainRepository;
import com.coding.xt.sso.model.params.LoginParam;
import com.coding.xt.sso.service.LoginService;
import me.chanjar.weixin.mp.api.WxMpService;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.joda.time.DateTime;


/**
 * @Author yaCoding
 * @create 2022-09-17 下午 9:57
 */
//实现登录接口实现类
@Service
public class LoginServiceImpl extends AbstractService implements LoginService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxOpenConfig wxOpenConfig;

    @Autowired
    private LoginDomainRepository loginDomainRepository;

    @Override
    public CallResult getQRCodeUrl() {
//        // 生成 state 参数，用于防止 csrf//得到需要的
//        String date = new DateTime().toString("yyyyMMddHHmmss");
//        String state = DigestUtils.md5Hex(wxOpenConfig.csrfKey+date);
//        String url = wxMpService.buildQrConnectUrl(wxOpenConfig.redirectUrl, wxOpenConfig.scope, state);
//        return CallResult.success(url);
        LoginDomain loginDomain = loginDomainRepository.createDomain(new LoginParam());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>(){
            @Override
            public CallResult<Object> doAction() {
                return loginDomain.buildQrConnectUrl();
            }
        });
    }

    @Override
    @Transactional
    //微信接口回调
    public CallResult wxLoginCallBack(LoginParam loginParam) {
        LoginDomain loginDomain = loginDomainRepository.createDomain(loginParam);
        //带有事务的执行操作
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {

            @Override
            public CallResult<Object> checkBiz() {
                //检查业务参数
                return loginDomain.checkWxLoginCallBackBiz();
            }

            @Override
            public CallResult<Object> doAction() {
                //写业务逻辑的
                return loginDomain.wxLoginCallBack();
            }
        });
    }

    @Override
    public String authorize() {
        return null;
    }

    @Override
    public CallResult wxGzhLoginCallBack(LoginParam loginParam) {
        return null;
    }

}
