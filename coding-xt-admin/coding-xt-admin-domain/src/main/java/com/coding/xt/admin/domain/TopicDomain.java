package com.coding.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coding.xt.admin.domain.repository.TopicDomainRepository;
import com.coding.xt.admin.model.TopicModel;
import com.coding.xt.admin.params.TopicParam;
import com.coding.xt.common.enums.TopicType;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.model.ListPageModel;
import com.coding.xt.pojo.Subject;
import com.coding.xt.pojo.Topic;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yaCoding
 * @create 2022-09-22 下午 5:54
 */

public class TopicDomain {

    private TopicParam topicParam;

    private TopicDomainRepository topicDomainRepository;

    public TopicDomain(TopicParam topicParam, TopicDomainRepository topicDomainRepository) {
        this.topicParam = topicParam;
        this.topicDomainRepository = topicDomainRepository;
    }

    private String displayTopicType(Integer topicType) {
        switch (TopicType.valueOfCode(topicType)) {
            case FILL_BLANK:
                return "填空题";
            case RADIO:
                return "单选题";
            case QA:
                return "问答题";
            case MUL_CHOICE:
                return "多选题";
            case JUDGE:
                return "判断题";
            default:return "";
        }
    }

    private String displayTopicSubject(List<Subject> subjectList, Long subject) {
        for (Subject subject1: subjectList) {
            if (subject1.getId().equals(subject)) {
                return subject1.getSubjectName() + " " + subject1.getSubjectGrade() +" "+ subject1.getSubjectTerm();
            }
        }
        return "";
    }
    private TopicModel copy(Topic topic) {
        TopicModel topicModel = new TopicModel();
        BeanUtils.copyProperties(topic,topicModel);
        return topicModel;
    }
    private List<TopicModel> copyList(List<Topic> topicList){
        if (topicList == null){
            return null;
        }
        List<Subject> subjectList = this.topicDomainRepository.createSubjectDomain(null).findAllSubjectList();
        List<TopicModel> topicModelList = new ArrayList<>();
        for (Topic topic : topicList){
            TopicModel model = copy(topic);
            model.setTopicTypeStr(displayTopicType(topic.getTopicType()));
            model.setSubjectStr(displayTopicSubject(subjectList,topic.getTopicSubject()));
            model.setCreateTime(new DateTime(topic.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            model.setLastUpdateTime(new DateTime(topic.getLastUpdateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            topicModelList.add(model);
        }
        return topicModelList;
    }

    //主题域 --查找主题列表
    public CallResult<Object> findTopicList() {
        int page = this.topicParam.getPage();
        int pageSize = this.topicParam.getPageSize();
        String topicTitle = topicParam.getTopicTitle();
        Long subjectId = topicParam.getSubjectId();
        Page<Topic> topicList = this.topicDomainRepository.findPage(page,pageSize,topicTitle,subjectId);
        List<TopicModel> topicModelList = copyList(topicList.getRecords());
        ListPageModel<TopicModel> listModel = new ListPageModel<>();
        listModel.setList(topicModelList);
        listModel.setSize(topicList.getTotal());
        return CallResult.success(listModel);
    }

    //按标题查找主题
    public TopicModel findTopicByTitle() {
        Topic topic = this.topicDomainRepository.findTopicByTitle(this.topicParam.getTopicTitle());
        return copy(topic);
    }

    //更新主题
    public CallResult<Object> updateTopic() {
        Topic topic = new Topic();
        BeanUtils.copyProperties(this.topicParam,topic);
        this.topicDomainRepository.update(topic);
        return CallResult.success();
    }

    //保存主题
    public CallResult<Object> saveTopic() {
        Topic topic = new Topic();
        BeanUtils.copyProperties(this.topicParam,topic);
        this.topicDomainRepository.save(topic);
        return CallResult.success();
    }

}
