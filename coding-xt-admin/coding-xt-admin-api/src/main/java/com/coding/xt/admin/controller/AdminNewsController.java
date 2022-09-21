package com.coding.xt.admin.controller;

import com.coding.xt.admin.params.NewsParam;
import com.coding.xt.admin.service.NewsService;
import com.coding.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author yaCoding
 * @create 2022-09-21 上午 9:52
 */

@RestController
@RequestMapping("news")
public class AdminNewsController {

    @Autowired
    private NewsService newsService;

    //保存新闻数据
    @PostMapping(value = "save")
    public CallResult save(@RequestBody NewsParam newsParam){
        return newsService.save(newsParam);
    }

    //更新数据
    @PostMapping(value = "update")
    public CallResult update(@RequestBody NewsParam newsParam) {
        return newsService.update(newsParam);
    }

    @PostMapping(value = "findNewsById")
    public CallResult findNewsById(@RequestBody NewsParam newsParam) {
        return  newsService.findNewsById(newsParam);
    }

    @PostMapping(value = "findPage")
    public CallResult findPage(@RequestBody NewsParam newsParam) {
        return newsService.findPage(newsParam);
    }

    //上传图片
    @PostMapping(value = "upload")
    public CallResult upload(@RequestParam("imageFile") MultipartFile file){
        //图片往 七牛云上传
        //云存储 ，图片 用户访问的时候 需要消耗带宽，带宽如果都会占用完了，应用就无法访问
        //一张图片2M 10个人同时访问 20M 服务器带宽 10M左右
        //网络卡了，电脑需要用网络的应用 相当于卡了 如果图片的访问将服务器的带宽资源占用完，代表应用不能访问了
        //这是不可接受的，加大带宽，但是带宽的费用非常大，抖音 一天的带宽费用 上亿，我们的应用不是所有的情况都有如此大的流量的
        //间歇性，这时候 云存储，按量付费，花费较少，云存储 在各地都有服务器，可以针对性的对图片进行加速访问

        return newsService.upload(file);
    }

}
