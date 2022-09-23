package com.coding.xt.admin.controller;

import com.coding.xt.admin.params.SubjectParam;
import com.coding.xt.admin.service.SubjectService;
import com.coding.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:35
 */
//管理对象控制器
@RestController
@RequestMapping("subject")
public class AdminSubjectController {

    @Autowired
    private SubjectService subjectService;

    //发现页
    @RequestMapping(value = "findPage")
    public CallResult findPage(@RequestBody SubjectParam subjectParam) {
        return subjectService.findSubjectList(subjectParam);
    }

    //保存主题
    @PostMapping(value = "saveSubject")
    public CallResult saveSubject(@RequestBody SubjectParam subjectParam) {
        return subjectService.saveSubject(subjectParam);
    }

}
