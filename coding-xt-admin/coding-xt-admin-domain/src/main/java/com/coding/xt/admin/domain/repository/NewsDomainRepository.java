package com.coding.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.admin.dao.NewsMapper;
import com.coding.xt.admin.domain.NewsDomain;
import com.coding.xt.admin.domain.qiniu.QiniuConfig;
import com.coding.xt.admin.params.NewsParam;
import com.coding.xt.pojo.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:34
 */
//新闻域存储库
@Component
public class NewsDomainRepository {

    @Autowired
    public QiniuConfig qiniuConfig;

    @Resource
    private NewsMapper newsMapper;

    public NewsDomain createDomain(NewsParam newsParam) {
        return new NewsDomain(this,newsParam);
    }

    public Page<News> findNewsPageByCondition(int currentPage, int pageSize, String queryString) {
        Page<News> page = new Page<>(currentPage,pageSize);

        LambdaUpdateWrapper<News> queryWrapper = new LambdaUpdateWrapper<>();
        if (StringUtils.isNotBlank(queryString)){
            queryWrapper.like(News::getTitle,queryString);
        }
        return newsMapper.selectPage(page, queryWrapper);
    }

    public void save(News news) {
        this.newsMapper.insert(news);
    }

    public News findNewsById(Long id) {
        return newsMapper.selectById(id);
    }

    public void update(News news) {
        this.newsMapper.updateById(news);
    }
}
