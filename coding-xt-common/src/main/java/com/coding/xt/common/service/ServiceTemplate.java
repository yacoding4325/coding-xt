package com.coding.xt.common.service;


import com.coding.xt.common.model.CallResult;

/**
 * @author Jarno
 */
public interface ServiceTemplate {


    /**
     * run in  datasource and execute Transaction
     * @param action
     * @param <T>
     * @return
     */
    <T> CallResult<T> execute(TemplateAction<T> action);

    /**
     * run in  datasource and not execute Transaction
     * @param action
     * @param <T>
     * @return
     */
    <T> CallResult<T> executeQuery(TemplateAction<T> action);
}
