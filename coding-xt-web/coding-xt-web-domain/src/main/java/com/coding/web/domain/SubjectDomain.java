package com.coding.web.domain;

import com.coding.web.domain.repository.SubjectDomainRepository;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.utils.CommonUtils;
import com.coding.xt.pojo.Subject;
import com.coding.xt.pojo.SubjectUnit;
import com.coding.xt.web.model.SubjectModel;
import com.coding.xt.web.model.params.SubjectParam;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author yaCoding
 * @create 2022-09-25 上午 11:04
 */

public class SubjectDomain {

    private SubjectDomainRepository subjectDomainRepository;

    private SubjectParam subjectParam;

    public SubjectDomain(SubjectDomainRepository subjectDomainRepository, SubjectParam subjectParam) {
        this.subjectDomainRepository = subjectDomainRepository;
        this.subjectParam = subjectParam;
    }

    public List<SubjectModel> copyList(List<Subject> subjectList){

        List<SubjectModel> subjectModels = new ArrayList<>();
        for (Subject subject : subjectList) {
            SubjectModel target = new SubjectModel();
            BeanUtils.copyProperties(subject, target);
            subjectModels.add(target);
        }
        return subjectModels;
    }

    public CallResult<Object> listSubject() {
        /**
         * 1. 先查询所有的科目信息
         * 2. 将名字，年级，学期 组成set集合 去重
         * 3. 进行排序处理
         */
        List<Subject> subjectList = this.subjectDomainRepository.findSubjectList();
        Set<String> subjectNameList = new TreeSet<>();
        Set<String> subjectGradeList = new TreeSet<>();
        Set<String> subjectTermList = new TreeSet<>();
        for (Subject subject : subjectList) {
            subjectNameList.add(subject.getSubjectName());
            subjectGradeList.add(subject.getSubjectGrade());
            subjectTermList.add(subject.getSubjectTerm());
        }
        Set<String> sortSet = new TreeSet<>((o1, o2) -> {
            /**
             * 1. 要将年级名称 取第一个字 七 八 九
             * 2. 转换为 7,8,9
             * 3. 进行数字排序
             */
            String numberStr1 = CommonUtils.getNumberStr(o1);
            String numberStr2 = CommonUtils.getNumberStr(o2);
            long number1 = CommonUtils.chineseNumber2Int(numberStr1);
            long number2 = CommonUtils.chineseNumber2Int(numberStr2);
            return (int) (number1 - number2);
        });
        sortSet.addAll(subjectGradeList);
        Map<String, Set<String>> map = new HashMap<>();
        map.put("subjectNameList",subjectNameList);
        map.put("subjectGradeList",sortSet);
        map.put("subjectTermList",subjectTermList);
        return CallResult.success(map);
    }

    public List<SubjectModel> findSubjectListByCourseId(Long courseId) {
        List<Subject> subjectList = subjectDomainRepository.findSubjectListByCourseId(courseId);
        return copyList(subjectList);
    }

    public List<Integer> findSubjectUnitBySubjectId(Long subjectId) {
        List<SubjectUnit> subjectUnitList = this.subjectDomainRepository.findUnitBySubjectId(subjectId);
        return subjectUnitList.stream().map(SubjectUnit::getSubjectUnit).collect( Collectors.toList());
    }
}
