package com.coding.web.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.web.domain.repository.NewsDomainRepository;
import com.coding.xt.common.model.BusinessCodeEnum;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.model.ListPageModel;
import com.coding.xt.pojo.News;
import com.coding.xt.web.model.NewsModel;
import com.coding.xt.web.model.enums.TabEnum;
import com.coding.xt.web.model.params.NewsParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-19 下午 9:51
 */
//新闻域
public class NewsDomain {
    private NewsDomainRepository newsDomainRepository;
    private NewsParam newsParam;
    public NewsDomain(NewsDomainRepository newsDomainRepository, NewsParam newsParam) {
        this.newsDomainRepository = newsDomainRepository;
        this.newsParam = newsParam;
    }
    public NewsModel copy(News news){
        if (news == null){
            return null;
        }
        NewsModel newsModel = new NewsModel();
        //属性copy
        BeanUtils.copyProperties(news,newsModel);
        if (news.getCreateTime() != null) {
            newsModel.setCreateTime(new DateTime(news.getCreateTime()).toString("yyyy年MM月dd日 HH:mm:ss"));
        }
        if (news.getImageUrl() != null) {
            if (!news.getImageUrl().startsWith("http")) {
                newsModel.setImageUrl(newsDomainRepository.qiniuConfig.getFileServerUrl() + news.getImageUrl());
            }
        }
        return newsModel;
    }

    public List<NewsModel> copyList(List<News> newsList){
        List<NewsModel> newsModelList = new ArrayList<>();
        for (News news : newsList){
            newsModelList.add(copy(news));
        }
        return newsModelList;
    }

    public CallResult<Object> checkNewsListParam() {
        //检查 1. 分页参数 pagesize 大于5 pagesize=5
        //2. tab是否合法
        int pageSize = newsParam.getPageSize();
        if (pageSize > 5){
            newsParam.setPageSize(5);
        }
        int page = newsParam.getPage();
        if (page <= 0){
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(),"参数不正确");
        }
        Integer tab = newsParam.getTab();
        if (TabEnum.valueOfCode(tab) == null){
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(),"参数不正确");
        }
        return CallResult.success();
    }

    public CallResult<Object> newsList() {
        int page = newsParam.getPage();
        int pageSize = newsParam.getPageSize();
        Integer tab = newsParam.getTab();
        Page<News> newsPage = this.newsDomainRepository.findNewsListByTab(page,pageSize,tab);
        List<News> records = newsPage.getRecords();

        List<NewsModel> newsModelList = copyList(records);
        //分页模型，所有的分页 都需要返回此模型 固定模式
        ListPageModel listPageModel = new ListPageModel<>();
        listPageModel.setList(newsModelList);
        listPageModel.setPage(page);
        listPageModel.setPageSize(pageSize);
        listPageModel.setSize(newsPage.getTotal());
        listPageModel.setPageCount(newsPage.getPages());
        return CallResult.success(listPageModel);
    }

    public CallResult<Object> findNewsById() {
        News news = this.newsDomainRepository.findNewsById(newsParam.getId());
        NewsModel newsModel = copy(news);
        return CallResult.success(newsModel);
    }
}
