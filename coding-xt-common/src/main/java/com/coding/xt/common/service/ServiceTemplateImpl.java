package com.coding.xt.common.service;

import com.coding.xt.common.model.BusinessCodeEnum;
import com.coding.xt.common.model.CallResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @Author yaCoding
 * @create 2022-09-16 下午 8:55
 */
@Component
@Slf4j
public class ServiceTemplateImpl implements ServiceTemplate{
    @Override
    public <T> CallResult<T> execute(TemplateAction<T> action) {
        try{
            CallResult<T> callResult = action.checkParam();
            if(callResult==null){
                log.warn("execute: Null result while checkParam");
                return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getMsg());
            }
            if(!callResult.isSuccess()){
                return callResult;
            }
            callResult = action.checkBiz();
            if(callResult==null){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.warn("execute: Null result while checkBiz");
                return CallResult.fail(BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getMsg());
            }
            if(!callResult.isSuccess()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return callResult;
            }
            long start = System.currentTimeMillis();
            CallResult<T> cr= action.doAction();
            log.info("execute datasource method run time:{}ms", System.currentTimeMillis() - start);
            if (cr == null){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return CallResult.fail(BusinessCodeEnum.CHECK_ACTION_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_ACTION_NO_RESULT.getMsg());
            }
            if (!cr.isSuccess()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return cr;
            }
            if(cr.isSuccess()){
                action.finishUp(cr);
            }
            return cr;
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            log.error("excute error", e);
            return CallResult.fail();
        }
    }

    @Override
    public <T> CallResult<T> executeQuery(TemplateAction<T> action) {
        try{
            CallResult<T> callResult = action.checkParam();
            if(callResult==null){
                log.warn("executeQuery: Null result while checkParam");
                return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getMsg());
            }
            if(!callResult.isSuccess()){
                return callResult;
            }
            callResult = action.checkBiz();
            if(callResult==null){
                log.warn("executeQuery: Null result while checkBiz");
                return CallResult.fail(BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getMsg());
            }
            if(!callResult.isSuccess()){
                return callResult;
            }
            long start = System.currentTimeMillis();
            CallResult<T> cr= action.doAction();
            log.info("executeQuery datasource method run time:{}ms", System.currentTimeMillis() - start);
            if (cr == null){
                return CallResult.fail(BusinessCodeEnum.CHECK_ACTION_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_ACTION_NO_RESULT.getMsg());
            }
            if (!cr.isSuccess()){
                return cr;
            }
            if(cr.isSuccess()){
                action.finishUp(cr);
            }
            return cr;
        }catch(Exception e){
            e.printStackTrace();
            log.error("executeQuery error", e);
            return CallResult.fail();
        }
    }
}
