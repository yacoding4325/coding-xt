package com.coding.xt.web.api;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.model.service.NewsService;
import com.coding.xt.web.model.params.NewsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:03
 */
@RestController
@RequestMapping("news")
public class NewsApi {

    @Autowired
    private NewsService newsService;

    @PostMapping("newsList")
    public CallResult newsList(@RequestBody NewsParam newsParam){
        return newsService.newsList(newsParam);
    }

    @PostMapping("detail")
    public CallResult news(@RequestBody NewsParam newsParam){
        return newsService.findNewsById(newsParam);
    }

    @PostMapping("newsDetailList")
    public CallResult newsDetailList(@RequestBody NewsParam newsParam){
        return newsService.newsDetailList(newsParam);
    }

}
