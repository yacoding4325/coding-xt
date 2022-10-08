package com.coding.xt.admin.config;

import com.coding.xt.admin.template.StringTemplate;
import com.coding.xt.admin.template.TimeAgoMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author yaCoding
 * @create 2022-10-08 下午 9:54
 */

@Configuration
public class FreemarkerConfig {

    @Autowired
    private StringTemplate stringTemplate;

    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void init(){
        configuration.setSharedVariable(stringTemplate.getName(),stringTemplate);
        configuration.setSharedVariable("timeAgo",new TimeAgoMethod());
    }
}
