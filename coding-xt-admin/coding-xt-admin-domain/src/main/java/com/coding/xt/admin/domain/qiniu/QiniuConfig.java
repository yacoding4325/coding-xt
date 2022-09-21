package com.coding.xt.admin.domain.qiniu;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yaCoding
 * @create 2022-09-20 下午 9:40
 */

@Configuration
@Data
//七牛云的一些数据
public class QiniuConfig {

    @Value("${qiniu.file.server.url}")
    private String fileServerUrl;

    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.accessSecret}")
    private String accessSecret;
    @Value("${qiniu.bucket}")
    private String bucket;
}