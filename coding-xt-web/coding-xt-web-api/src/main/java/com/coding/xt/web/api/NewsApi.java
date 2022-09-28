package com.coding.xt.web.api;

import com.coding.xt.common.cache.Cache;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.service.NewsService;
import com.coding.xt.web.model.params.NewsParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:03
 */
@RestController
@RequestMapping("news")
@Slf4j
public class NewsApi {

    @Autowired
    private NewsService newsService;

    @PostMapping("newsList")
//    @Cache(name = "newsList",time = 2*60)
    public CallResult newsList(@RequestBody NewsParam newsParam){
        log.info("新闻管理列表访问参数:{}",newsParam);
        return newsService.newsList(newsParam);
    }

    @PostMapping("detail")
    @Cache(name = "newsDetail",time = 30)
    public CallResult news(@RequestBody NewsParam newsParam){
        return newsService.findNewsById(newsParam);
    }
//
//    @PostMapping("newsDetailList")
//    public CallResult newsDetailList(@RequestBody NewsParam newsParam){
//        return newsService.newsDetailList(newsParam);
//    }
}
