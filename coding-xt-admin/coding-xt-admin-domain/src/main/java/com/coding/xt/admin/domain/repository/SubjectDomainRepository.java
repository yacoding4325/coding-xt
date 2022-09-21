package com.coding.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.admin.dao.SubjectMapper;
import com.coding.xt.admin.dao.SubjectUnitMapper;
import com.coding.xt.admin.domain.SubjectDomain;
import com.coding.xt.admin.params.SubjectParam;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.pojo.Subject;
import com.coding.xt.pojo.SubjectUnit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yaCoding
 * @create 2022-09-21 下午 8:42
 */

@Component
public class SubjectDomainRepository {

    @Resource
    private SubjectMapper subjectMapper;

    @Resource
    private SubjectUnitMapper subjectUnitMapper;


    public SubjectDomain createDomain(SubjectParam subjectParam) {
        return new SubjectDomain(this,subjectParam);
    }

    public Page<Subject> findSubjectListPage(int currentPage, int pageSize, String queryString) {
        Page<Subject> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryString)){
            queryWrapper.like(Subject::getSubjectName,queryString);
        }
        return subjectMapper.selectPage(page, queryWrapper);
    }

    public Subject findSubjectByCondition(String subjectName, String subjectGrade, String subjectTerm) {

        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getSubjectName,subjectName)
                .eq(Subject::getSubjectGrade,subjectGrade)
                .eq(Subject::getSubjectTerm,subjectTerm);
        queryWrapper.last("limit 1");
        return subjectMapper.selectOne(queryWrapper);
    }

    public void save(Subject subject) {
        this.subjectMapper.insert(subject);
    }

    public void saveSubjectUnit(Long subjectId, Integer subjectUnit) {
        SubjectUnit subjectUnit1 = new SubjectUnit();
        subjectUnit1.setSubjectId(subjectId);
        subjectUnit1.setSubjectUnit(subjectUnit);
        subjectUnitMapper.insert(subjectUnit1);
    }

    public Subject findSubjectById(Long id) {
        return subjectMapper.selectById(id);
    }

    public List<Integer> findSubjectUnitsById(Long subjectId) {
        LambdaQueryWrapper<SubjectUnit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SubjectUnit::getSubjectUnit);
        queryWrapper.eq(SubjectUnit::getSubjectId,subjectId);
        List<SubjectUnit> subjectUnits = subjectUnitMapper.selectList(queryWrapper);
        return subjectUnits.stream().map(SubjectUnit::getSubjectUnit).collect(Collectors.toList());
    }

    public void update(Subject subject) {
        this.subjectMapper.updateById(subject);
    }

    public void deleteUnitBySubjectId(Long subjectId) {
        LambdaQueryWrapper<SubjectUnit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectUnit::getSubjectId,subjectId);
        this.subjectUnitMapper.delete(queryWrapper);
    }

    public List<Subject> findAll() {
        return subjectMapper.selectList(new LambdaQueryWrapper<>());
    }

    public List<Subject> findSubjectListByCourseId(Long courseId) {
        return this.subjectMapper.findSubjectListByCourseId(courseId);
    }
}
