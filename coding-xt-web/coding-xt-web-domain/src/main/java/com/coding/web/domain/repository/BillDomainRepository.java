package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coding.web.domain.BillDomain;
import com.coding.xt.pojo.Bill;
import com.coding.xt.web.dao.BillMapper;
import com.coding.xt.web.model.enums.DeleteStatus;
import com.coding.xt.web.model.params.BillParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 3:47
 */
@Component
public class BillDomainRepository {

    @Value("${invite.url}")
    public String inviteUrl;

    @Resource
    private BillMapper billMapper;

    public BillDomain createDomain(BillParam billParam) {
        return new BillDomain(this,billParam);
    }

    public Bill findBill(Long id) {
        return billMapper.selectById(id);
    }

    public List<Bill> findAll() {
        LambdaQueryWrapper<Bill> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Bill::getDeleteStatus, DeleteStatus.NORMAL.getCode());
        return billMapper.selectList(queryWrapper);
    }
}
