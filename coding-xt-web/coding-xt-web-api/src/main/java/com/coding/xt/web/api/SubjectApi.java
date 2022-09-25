package com.coding.xt.web.api;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yaCoding
 * @create 2022-09-24 下午 9:25
 */

/**
 * 开发课程列表
 */
@RestController
@RequestMapping("subject")
public class SubjectApi {

    @Autowired
    private SubjectService subjectService;

    //列表主题
    @PostMapping(value = "listSubjectNew")
    public CallResult listSubjectNew(){
        return subjectService.listSubject();
    }

}
