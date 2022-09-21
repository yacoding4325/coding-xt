package com.coding.xt.admin.service.impl;

import com.coding.xt.admin.domain.SubjectDomain;
import com.coding.xt.admin.domain.repository.SubjectDomainRepository;
import com.coding.xt.admin.params.SubjectParam;
import com.coding.xt.admin.service.SubjectService;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:21
 */
@Service
@Transactional
public class SubjectServiceImpl extends AbstractService implements SubjectService {

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    //查找主题列表
    @Override
    public CallResult findSubjectList(SubjectParam subjectParam) {
        SubjectDomain subjectDomain = this.subjectDomainRepository.createDomain(subjectParam);

        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.findSubjectList();
            }
        });
    }


}
