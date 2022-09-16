package com.coding.xt.common.service;


import com.coding.xt.common.model.CallResult;

/**
 * @author Jarno
 */
public abstract class AbstractTemplateAction<T> implements TemplateAction<T> {
    @Override
    public CallResult<T> checkParam() {
        return CallResult.success();
    }

    @Override
    public CallResult<T> checkBiz() {
        return CallResult.success();
    }

    @Override
    public void finishUp(CallResult<T> callResult) {

    }
}
