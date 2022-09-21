package com.coding.xt.admin.service;

import com.coding.xt.admin.params.NewsParam;
import com.coding.xt.common.model.CallResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author yaCoding
 * @create 2022-09-21 上午 9:30
 */
//新闻接口
public interface NewsService {

    CallResult findPage(NewsParam newsParam);

    CallResult save(NewsParam newsParam);

    CallResult findNewsById(NewsParam newsParam);

    CallResult update(NewsParam newsParam);

    CallResult upload(MultipartFile file);

}
