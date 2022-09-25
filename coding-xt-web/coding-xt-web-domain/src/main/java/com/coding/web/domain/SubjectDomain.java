package com.coding.web.domain;

import com.coding.web.domain.repository.SubjectDomainRepository;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.utils.CommonUtils;
import com.coding.xt.pojo.Subject;
import com.coding.xt.web.model.SubjectModel;
import com.coding.xt.web.model.params.SubjectParam;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * @Author yaCoding
 * @create 2022-09-25 上午 11:04
 */

public class SubjectDomain {

    private SubjectParam subjectParam;
    private SubjectDomainRepository subjectDomainRepository;

    public SubjectDomain( SubjectDomainRepository subjectDomainRepository,SubjectParam subjectParam) {
        this.subjectParam = subjectParam;
        this.subjectDomainRepository = subjectDomainRepository;
    }

    public CallResult<Object> listSubject() {
        List<Subject> subjectList = this.subjectDomainRepository.findSubjectList();
        List<SubjectModel> subjectModelList = copyList(subjectList);
        Set<String> subjectNameList = new TreeSet<>();
        Set<String> subjectGradeList = new TreeSet<>();
        Set<String> subjectTermList = new TreeSet<>();
        for (SubjectModel subjectModel : subjectModelList) {
            subjectNameList.add(subjectModel.getSubjectName());
            subjectGradeList.add(subjectModel.getSubjectGrade());
            subjectTermList.add(subjectModel.getSubjectTerm());
        }
        Set<String> sortSet = new TreeSet<String>((o1, o2) -> {
            String numberStr1 = CommonUtils.getNumberStr(o1);
            String numberStr2 = CommonUtils.getNumberStr(o2);
            long num1 = CommonUtils.chineseNumber2Int(numberStr1);
            long num2 = CommonUtils.chineseNumber2Int(numberStr2);
            return (int) (num1 - num2);
        });
        sortSet.addAll(subjectGradeList);
        Map<String,Set<String>> map = new HashMap<>();
        map.put("subjectNameList",subjectNameList);
        map.put("subjectGradeList",sortSet);
        map.put("subjectTermList",subjectTermList);
        return CallResult.success(map);
    }

    private List<SubjectModel> copyList(List<Subject> subjectList) {
        List<SubjectModel> subjectModels = new ArrayList<>();
        for (Subject subject : subjectList) {
            SubjectModel target = new SubjectModel();
            BeanUtils.copyProperties(subject, target);
            subjectModels.add(target);
        }
        return subjectModels;
    }

}
