package com.coding.xt.web.service;


import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.model.params.NewsParam;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:20
 */


public interface NewsService {

    CallResult newsList(NewsParam newsParam);

    CallResult newsDetailList(NewsParam newsParam);

    CallResult findNewsById(NewsParam newsParam);

}
