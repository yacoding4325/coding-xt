package com.coding.web.service.impl;


import com.coding.web.domain.NewsDomain;
import com.coding.web.domain.repository.NewsDomainRepository;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import com.coding.xt.model.service.NewsService;
import com.coding.xt.web.model.params.NewsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:44
 */
@Service
public class NewsServiceImpl extends AbstractService implements NewsService {

    @Autowired
    private NewsDomainRepository newsDomainRepository;

    @Override
    public CallResult newsList(NewsParam newsParam) {
        NewsDomain newsDomain = this.newsDomainRepository.createDomain(newsParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> checkParam() {
                //参数检测
                return newsDomain.checkNewsListParam();
            }

            @Override
            public CallResult<Object> doAction() {
                return newsDomain.newsList(false);
            }
        });
    }

    @Override
    public CallResult newsDetailList(NewsParam newsParam) {
        NewsDomain newsDomain = this.newsDomainRepository.createDomain(newsParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return newsDomain.newsList(true);
            }
        });
    }

    @Override
    public CallResult findNewsById(NewsParam newsParam) {
        /**
         *
         */
        NewsDomain newsDomain = this.newsDomainRepository.createDomain(newsParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return newsDomain.findNewsById();
            }
        });
    }


}
