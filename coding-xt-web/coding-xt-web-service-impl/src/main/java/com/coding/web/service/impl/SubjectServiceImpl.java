package com.coding.web.service.impl;

import com.coding.web.domain.SubjectDomain;
import com.coding.web.domain.repository.SubjectDomainRepository;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import com.coding.xt.web.model.params.SubjectParam;
import com.coding.xt.web.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yaCoding
 * @create 2022-09-25 上午 11:01
 */

@Service
public class SubjectServiceImpl extends AbstractService implements SubjectService {

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Override
    public CallResult listSubject() {
        SubjectDomain subjectDomain = subjectDomainRepository.createDomain(new SubjectParam());
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.listSubject();
            }
        });
    }
}
