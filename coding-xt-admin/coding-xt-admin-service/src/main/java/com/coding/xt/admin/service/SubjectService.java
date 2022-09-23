package com.coding.xt.admin.service;

import com.coding.xt.admin.params.SubjectParam;
import com.coding.xt.common.model.CallResult;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:20
 */

public interface SubjectService {
    /**
     * 查找主题列表
     * @param subjectParam
     * @return
     */
    CallResult findSubjectList(SubjectParam subjectParam);

    /**
     * 保存主题列表
     * @param subjectParam
     * @return
     */
    CallResult saveSubject(SubjectParam subjectParam);
}
