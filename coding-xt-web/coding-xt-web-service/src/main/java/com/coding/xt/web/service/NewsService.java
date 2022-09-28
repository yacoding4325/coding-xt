package com.coding.xt.web.service;


import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.NewsParam;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:20
 */


public interface NewsService {

    /**
     * 分页查询 新闻列表
     * @param newsParam
     * @return
     */
    CallResult newsList(NewsParam newsParam);

    CallResult findNewsById(NewsParam newsParam);

}
