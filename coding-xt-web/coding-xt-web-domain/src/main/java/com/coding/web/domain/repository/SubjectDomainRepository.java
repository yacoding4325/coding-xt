package com.coding.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coding.web.domain.SubjectDomain;
import com.coding.xt.common.enums.Status;
import com.coding.xt.pojo.Subject;
import com.coding.xt.web.dao.SubjectMapper;
import com.coding.xt.web.model.params.SubjectParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-25 上午 11:04
 */
@Component
public class SubjectDomainRepository {

    @Resource
    private SubjectMapper subjectMapper;

    public SubjectDomain createDomain(SubjectParam subjectParam) {
        return new SubjectDomain(this,subjectParam);
    }

    public List<Subject> findSubjectList() {
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getStatus, Status.NORMAL.getCode());
        return subjectMapper.selectList(queryWrapper);
    }

    public List<Subject> findSubjectListByCourseId(Long courseId) {
        return subjectMapper.findSubjectListByCourseId(courseId);
    }

}
