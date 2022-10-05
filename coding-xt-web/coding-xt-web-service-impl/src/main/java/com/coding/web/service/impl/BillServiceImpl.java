package com.coding.web.service.impl;

import com.coding.web.domain.BillDomain;
import com.coding.web.domain.repository.BillDomainRepository;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import com.coding.xt.web.model.params.BillParam;
import com.coding.xt.web.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 3:47
 */
@Service
public class BillServiceImpl extends AbstractService implements BillService {

    @Autowired
    private BillDomainRepository billDomainRepository;

    @Override
    public CallResult all(BillParam billParam) {
        BillDomain billDomain = this.billDomainRepository.createDomain(billParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return billDomain.findAllBillModelList();
            }
        });
    }

    @Override
    public CallResult gen(BillParam billParam) {
        BillDomain billDomain = this.billDomainRepository.createDomain(billParam);
        return billDomain.gen();
    }

}
