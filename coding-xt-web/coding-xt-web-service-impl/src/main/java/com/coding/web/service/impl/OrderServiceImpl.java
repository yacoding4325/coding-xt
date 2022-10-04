package com.coding.web.service.impl;

import com.coding.web.domain.OrderDomain;
import com.coding.web.domain.repository.OrderDomainRepository;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.service.AbstractTemplateAction;
import com.coding.xt.web.model.params.OrderParam;
import com.coding.xt.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:48
 */
@Service
public class OrderServiceImpl extends AbstractService implements OrderService {

    @Autowired
    private OrderDomainRepository orderDomainRepository;

    @Override
    public CallResult submitOrder(OrderParam orderParam) {
        OrderDomain orderDomain = this.orderDomainRepository.createDomain(orderParam);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.submitOrder();
            }
        });
    }

    @Override
    public CallResult wxPay(OrderParam orderParam) {
        OrderDomain orderDomain = this.orderDomainRepository.createDomain(orderParam);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.wxPay();
            }
        });
    }

    @Override
    public CallResult notifyOrder(String xmlData) {
        OrderDomain orderDomain = orderDomainRepository.createDomain(null);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.notifyOrder(xmlData);
            }
        });
    }

    @Override
    public CallResult findOrder(OrderParam orderParam) {
        OrderDomain orderDomain = orderDomainRepository.createDomain(orderParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.findOrder();
            }
        });
    }

    @Override
    public CallResult orderList(OrderParam orderParam) {
        OrderDomain orderDomain = this.orderDomainRepository.createDomain(orderParam);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.orderList();
            }
        });
    }
}
