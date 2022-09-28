package com.coding.web.domain;

import ch.qos.logback.core.status.ErrorStatus;
import com.alibaba.fastjson.JSON;
import com.coding.web.domain.repository.TopicDomainRepository;
import com.coding.xt.common.enums.TopicType;
import com.coding.xt.common.login.UserThreadLocal;
import com.coding.xt.common.model.BusinessCodeEnum;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.model.topic.ContentAndImage;
import com.coding.xt.common.model.topic.FillBlankChoice;
import com.coding.xt.pojo.*;
import com.coding.xt.web.dao.TopicDTO;
import com.coding.xt.web.model.*;
import com.coding.xt.web.model.enums.HistoryStatus;
import com.coding.xt.web.model.params.TopicParam;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:08
 */

public class TopicDomain {

    private TopicDomainRepository topicDomainRepository;
    private TopicParam topicParam;
    private Topic topic;
    private UserHistory userHistory;

    public TopicDomain(TopicDomainRepository topicDomainRepository, TopicParam topicParam) {
        this.topicDomainRepository = topicDomainRepository;
        this.topicParam = topicParam;
    }

    public CallResult<Object> checkPracticeBiz() {
        Long subjectId = topicParam.getSubjectId();
        Long userId = UserThreadLocal.get();
        List<Long> courseIdList = this.topicDomainRepository.getCourseDomain(null).findCourseIdBySubject(subjectId);
        if (courseIdList == null || courseIdList.size()<=0){
            return CallResult.fail(BusinessCodeEnum.COURSE_NO_BUY.getCode(),"not buy course");
        }
        Integer orderCount = this.topicDomainRepository.getUserCourseDomain(null).countUserCourseByUserId(userId,courseIdList,System.currentTimeMillis());
        if (orderCount == null || orderCount == 0){
            return CallResult.fail(BusinessCodeEnum.COURSE_NO_BUY.getCode(),"not buy course");
        }
        return CallResult.success();
    }

    public CallResult<Object> practice() {
        Long userId = UserThreadLocal.get();
        Long subjectId = topicParam.getSubjectId();
        String topicAreaPro = topicParam.getTopicAreaPro();
        Long practiceId = topicParam.getPracticeId();
        UserHistory userHistory = null;
        //如果是从我的学习中 直接进入，根据学习记录id 进行查询
        //如果不是，根据传递的学科id，进行查询，判断是否进行过学习，上一次学习未完成不能开启下一次练习
        if (practiceId == null) {
            userHistory = this.topicDomainRepository.createHistoryDomain(null).findUserHistory(userId, subjectId, HistoryStatus.NO_FINISH.getCode());
        }else{
            userHistory = this.topicDomainRepository.createHistoryDomain(null).findUserHistoryById(userId,practiceId);
        }
        if (userHistory == null){
            //新的练习
            return startNewStudy(subjectId,userId,topicAreaPro);
        }
        if (userHistory.getHistoryStatus() == 3){
            return CallResult.fail(BusinessCodeEnum.PRACTICE_CANCEL.getCode(),BusinessCodeEnum.PRACTICE_CANCEL.getMsg());
        }
        //已有的练习题，开始练习
        Integer progress = userHistory.getProgress();
        Long topicId = this.topicDomainRepository.createUserPracticeDomain(null).findUserPractice(userId,userHistory.getId(), progress);
        if (topicId == null){
            return CallResult.fail();
        }
        System.out.println("已有练习题："+topicId);
        TopicDTO topic = this.topicDomainRepository.findTopicAnswer(topicId, userHistory.getId());
        PracticeDetailModel practiceModel = new PracticeDetailModel();
        practiceModel.setProgress(progress);
        practiceModel.setTotal(userHistory.getTopicTotal());
        practiceModel.setTopic(getTopicModelView(topic));
        practiceModel.setPracticeId(userHistory.getId());
        practiceModel.setPracticeId(userHistory.getId());
        int answered = userHistory.getProgress();
        if (answered != userHistory.getTopicTotal()){
            answered = userHistory.getProgress()-1;
        }
        practiceModel.setAnswered(answered);
        int trueNum = this.topicDomainRepository.createUserPracticeDomain(null).countUserPracticeTrueNum(userId,userHistory.getId());
        int wrongNum = this.topicDomainRepository.createUserPracticeDomain(null).countUserPracticeWrongNum(userId,userHistory.getId());
        practiceModel.setTrueNum(trueNum);
        practiceModel.setWrongNum(wrongNum);
        practiceModel.setNoAnswer(0);
        SubjectModel subject = this.topicDomainRepository.createSubjectDomain(null).findSubject(userHistory.getSubjectId());
        practiceModel.setSubjectName(subject.getSubjectName()+" "+subject.getSubjectGrade()+" "+subject.getSubjectTerm());
        List<Integer> subjectUnitList1 = JSON.parseArray(userHistory.getSubjectUnits(), Integer.class);
        practiceModel.setSubjectUnitList(subjectUnitList1);
        practiceModel.setCreateTime(new DateTime(userHistory.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        practiceModel.setFinishTime(userHistory.getFinishTime() == 0 ? "":new DateTime(userHistory.getFinishTime()).toString("yyyy-MM-dd HH:mm:ss"));
        practiceModel.setUseTime(userHistory.getFinishTime() == 0 ? "":useTime(userHistory.getFinishTime(),userHistory.getCreateTime()));
        List<Map<String,Object>> topicAnswerStatusList = this.topicDomainRepository.createUserPracticeDomain(null).findUserPracticeAll(userId,userHistory.getId());
        practiceModel.setTopicAnswerStatusList(topicAnswerStatusList);
        return CallResult.success(practiceModel);
    }

    private CallResult startNewStudy(Long subjectId,Long userId,String topicAreaPro) {
        List<Integer> subjectUnitList = topicParam.getSubjectUnitList();
        if (subjectUnitList == null){
            subjectUnitList = new ArrayList<>();
        }
        //随机50道题，开始新的练习
        List<Long> topicIdList = this.topicDomainRepository.findTopicRandomList(subjectId,topicAreaPro,subjectUnitList);
        if (topicIdList.size() <= 0){
            return CallResult.fail(BusinessCodeEnum.TOPIC_NO_PRACTICE.getCode(),"没有练习题，请正确选择科目,单元以及地区");
        }

        UserHistory userHistory = new UserHistory();
        userHistory.setCreateTime(System.currentTimeMillis());
        userHistory.setHistoryStatus(HistoryStatus.NO_FINISH.getCode());
        userHistory.setSubjectId(subjectId);
        userHistory.setProgress(1);
        userHistory.setSubjectUnits(JSON.toJSONString(subjectUnitList));
        userHistory.setTopicTotal(topicIdList.size());
        if (StringUtils.isEmpty(topicAreaPro)){
            topicAreaPro = "全国";
        }
        userHistory.setTopicPro(topicAreaPro);
        userHistory.setUserId(userId);
        userHistory.setFinishTime(0L);
        userHistory.setErrorCount(0);
        this.topicDomainRepository.createHistoryDomain(null).saveUserHistory(userHistory);
        //用户的练习题
        for (Long topicId : topicIdList){
            UserPractice userPractice = new UserPractice();
            userPractice.setHistoryId(userHistory.getId());
            userPractice.setUserId(userId);
            userPractice.setTopicId(topicId);
            userPractice.setPStatus(0);
            userPractice.setUserAnswer("");
            this.topicDomainRepository.createUserPracticeDomain(null).saveUserPractice(userPractice);
        }

        TopicDTO topic = this.topicDomainRepository.findTopicAnswer(topicIdList.get(0), userHistory.getId());
        TopicModelView topicModelView = getTopicModelView(topic);
        PracticeDetailModel practiceModel = new PracticeDetailModel();
        practiceModel.setTotal(userHistory.getTopicTotal());
        practiceModel.setPracticeId(userHistory.getId());
        int answered = userHistory.getProgress();
        if (answered != userHistory.getTopicTotal()){
            answered = userHistory.getProgress()-1;
        }
        practiceModel.setAnswered(answered);
        practiceModel.setPracticeId(userHistory.getId());
        int trueNum = this.topicDomainRepository.createUserPracticeDomain(null).countUserPracticeTrueNum(userHistory.getId());
        int wrongNum = this.topicDomainRepository.createUserPracticeDomain(null).countUserPracticeWrongNum(userHistory.getId());
        practiceModel.setTrueNum(trueNum);
        practiceModel.setWrongNum(wrongNum);
        practiceModel.setNoAnswer(0);
        SubjectModel subject = this.topicDomainRepository.createSubjectDomain(null).findSubject(userHistory.getSubjectId());
        practiceModel.setSubjectName(subject.getSubjectName()+" "+subject.getSubjectGrade()+" "+subject.getSubjectTerm());
        List<Integer> subjectUnitList1 = JSON.parseArray(userHistory.getSubjectUnits(), Integer.class);
        practiceModel.setSubjectUnitList(subjectUnitList1);
        practiceModel.setCreateTime(new DateTime(userHistory.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        practiceModel.setFinishTime(userHistory.getFinishTime() == 0 ? "":new DateTime(userHistory.getFinishTime()).toString("yyyy-MM-dd HH:mm:ss"));
        practiceModel.setUseTime(userHistory.getFinishTime() == 0 ? "":useTime(userHistory.getFinishTime(),userHistory.getCreateTime()));
        practiceModel.setTopic(topicModelView);
        practiceModel.setProgress(1);
        List<Map<String,Object>> topicAnswerStatusList = this.topicDomainRepository.createUserPracticeDomain(null).findUserPracticeAll(userId,userHistory.getId());
        practiceModel.setTopicAnswerStatusList(topicAnswerStatusList);
        return CallResult.success(practiceModel);
    }

    private String useTime(Long finishTime, Long createTime) {
        long diff = finishTime - createTime;
        long dayTime = 24 * 60 * 60 * 1000;
        String useTime = "";
        long day = diff / dayTime;
        if (day > 0){
            useTime = day +"天";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(finishTime);
        int finishHour = calendar.get(Calendar.HOUR_OF_DAY);
        int finishMinute = calendar.get(Calendar.MINUTE);
        int finishSecond = calendar.get(Calendar.SECOND);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(createTime);
        int createHour = calendar1.get(Calendar.HOUR_OF_DAY);
        int createMinute = calendar1.get(Calendar.MINUTE);
        int createSecond = calendar1.get(Calendar.SECOND);
        int diffHour = finishHour - createHour;
        if (diffHour < 0){
            diffHour = -diffHour;
        }
        if (diffHour < 10){
            useTime += "0"+diffHour +":";
        }else {
            useTime += diffHour + ":";
        }
        int diffMinute = finishMinute - createMinute;
        if (diffMinute < 0){
            diffMinute = -diffMinute;
        }
        if (diffMinute < 10){
            useTime += "0"+diffMinute + ":";
        }else {
            useTime += diffMinute + ":";
        }
        int diffSecond = finishSecond - createSecond;
        if (diffSecond < 0){
            diffSecond = -diffSecond;
        }
        if (diffSecond < 10){
            useTime += "0"+ diffSecond;
        }else {
            useTime += String.valueOf(diffSecond);
        }
        return useTime;
    }

    private TopicModelView copyView(TopicDTO topic){
        TopicModelView topicModel = new TopicModelView();
        if (topic == null){
            return null;
        }
        BeanUtils.copyProperties(topic,topicModel);
        return topicModel;
    }

    private TopicModelView getTopicModelView(TopicDTO topic) {
        TopicModelView topicModel = copyView(topic);
        if (topic == null){
            return null;
        }
        String topicImg = topic.getTopicImg();
        if (!StringUtils.isEmpty(topicImg)){
            List<String> topicImgList = JSON.parseArray(topicImg, String.class);
            topicModel.setTopicImgList(topicImgList);
        }
        if (topic.getTopicType() == TopicType.FILL_BLANK.getCode()){
            List<FillBlankChoice> fillBlankChoiceList = JSON.parseArray(topic.getTopicChoice(), FillBlankChoice.class);
            topicModel.setFillBlankTopicChoice(fillBlankChoiceList.size());
            topicModel.setFillBlankAnswer(fillBlankChoiceList);
            topicModel.setAnswer(null);
            String userAnswer = topic.getUserAnswer();
            if (!StringUtils.isEmpty(userAnswer)){
                String[] split = userAnswer.split("\\$#\\$");
                String sss = "";
                for (String s : split) {
                    sss += s + "  ";
                }
                topicModel.setUserAnswer(sss);
            }
        }
        if (topic.getTopicType() == TopicType.RADIO.getCode()){
            List<Map<String, ContentAndImage>> list = JSON.parseObject(topic.getTopicChoice(), new com.alibaba.fastjson.TypeReference<List<Map<String,ContentAndImage>>>(){});
            topicModel.setRadioChoice(list);
        }
        if (topic.getTopicType() == TopicType.MUL_CHOICE.getCode()){
            List<Map<String,ContentAndImage>> list = JSON.parseObject(topic.getTopicChoice(), new com.alibaba.fastjson.TypeReference<List<Map<String,ContentAndImage>>>(){});
            topicModel.setMulChoice(list);
        }

        topicModel.setLastUpdateTime(new DateTime(topic.getLastUpdateTime()).toString("yyyy-MM-dd"));
        return topicModel;
    }

    //业务检查
    public CallResult<Object> checkSubmitBiz() {
        Long topicId = topicParam.getTopicId();
        Long practiceId = topicParam.getPracticeId();
        Long userId = UserThreadLocal.get();
        Topic topic = this.topicDomainRepository.findTopicById(topicId);
        if (topic == null) {
            return CallResult.fail(BusinessCodeEnum.TOPIC_NOT_EXIST.getCode(),"topic not exist");
        }
        UserHistory userHistory = this.topicDomainRepository.createHistoryDomain(null).findUserHistoryById(userId,practiceId);
        if (userHistory == null) {
            return CallResult.fail(BusinessCodeEnum.PRACTICE_NO_EXIST.getCode(),"练习题不存在");
        }
        if (userHistory.getHistoryStatus() == 2) {
            return CallResult.fail(BusinessCodeEnum.PRACTICE_FINISHED.getCode(),BusinessCodeEnum.PRACTICE_FINISHED.getMsg());
        }
        if (userHistory.getHistoryStatus() == 3) {
            return CallResult.fail(BusinessCodeEnum.PRACTICE_CANCEL.getCode(),BusinessCodeEnum.PRACTICE_CANCEL.getMsg());
        }
        this.topic = topic;
        this.userHistory = userHistory;
        return CallResult.success();
    }

    //执行业务
    public CallResult<Object> submit() {
        Long topicId = topicParam.getTopicId();
        Long userId = UserThreadLocal.get();
        String answer = topicParam.getAnswer();
        boolean finish = false;
        boolean answerFlag = false;
        //1. 填空题 选择题 判断题
        //2
        String topicAnswer = topic.getTopicAnswer();
        Integer topicType = topic.getTopicType();
        if (topicType == TopicType.FILL_BLANK.getCode()) {
            if (!StringUtils.isEmpty(answer)) {
                String[] str = answer.split("\\$#\\$");
                TopicDTO topicDTO = new TopicDTO();
                BeanUtils.copyProperties(topic, topicDTO);
                TopicModelView topicModel = getTopicModelView(topicDTO);
                if (str.length == topicModel.getFillBlankTopicChoice()) {
                    for (int i = 0; i < str.length; i++) {
                        FillBlankChoice anObject = topicModel.getFillBlankAnswer().get(i);
                        Logger log = null;
                        log.info("提交答案:{}", str[i]);
                        log.info("正确答案:{}", anObject);
                        if (str[i].equals(anObject.getContent())) {
                            answerFlag = true;
                        } else {
                            answerFlag = false;
                            break;
                        }
                    }
                }
            }
        }
        if (topicType == TopicType.RADIO.getCode()) {
            if (topicAnswer.equals(answer)) {
                answerFlag = true;
            }
        }
        if (topicType == TopicType.MUL_CHOICE.getCode()) {
            //数据库 A,B,C 前端传递的答案 A,B,C
            if (topicAnswer.equals(answer)) {
                answerFlag = true;
            }
        }
        if (topicType == TopicType.JUDGE.getCode()) {
            if (topicAnswer.equals(answer)) {
                answerFlag = true;
            }
        }
        UserHistoryDomain historyDomain = this.topicDomainRepository.createUserHistoryDomain(null);
        UserProblemDomain userProblemDomain = this.topicDomainRepository.createUserProblem(null);
        UserPracticeDomain userPracticeDomain = this.topicDomainRepository.createUserPracticeDomain(null);

        if (!answerFlag) {
            //要加入错题本
//加入错题本
            UserProblem userProblem = userProblemDomain.getUserProblem(userId, topicId);
            if (userProblem == null) {
                userProblem = new UserProblem();
                userProblem.setErrorCount(1);
                userProblem.setErrorStatus(ErrorStatus.ERROR);
                userProblem.setUserId(userId);
                userProblem.setTopicId(topicId);
                userProblem.setSubjectId(topicParam.getSubjectId());
                userProblem.setErrorAnswer(answer);
                userProblem.setErrorTime(System.currentTimeMillis());
                userProblemDomain.saveUserProblem(userProblem);
            } else {
                userProblemDomain.updateUserProblemErrorCount(userId, topicId, answer);
            }
            userPracticeDomain.updateUserPractice(userHistory.getId(), topicId, userId, answer, 1);
        } else {
            userPracticeDomain.updateUserPractice(userHistory.getId(), topicId, userId, answer, 2);
        }
        Integer progress = userHistory.getProgress();
        if (progress >= userHistory.getTopicTotal()) {
            //已经是最后一道题了,更新状态已完成和完成时间
            historyDomain.updateUserHistoryStatus(userHistory.getId(), HistoryStatus.FINISHED.getCode(), System.currentTimeMillis());
            finish = true;
        }
        TopicSubmitModel topicSubmitModel = new TopicSubmitModel();
        //答案是否正确
        topicSubmitModel.setAnswerFlag(answerFlag);
        topicSubmitModel.setFinish(finish);
        //答案 如果是填空题 并且有多个空 前端 传递过来的  第一个答案$#$第二个答案...
        //给前端返回的是 此道题的正确答案
        topicSubmitModel.setAnswer(topicAnswer);
        if (topic.getTopicType() == TopicType.FILL_BLANK.getCode()){
            TopicDTO topicDTO = new TopicDTO();
            BeanUtils.copyProperties(topic, topicDTO);
            TopicModelView topicModel = getTopicModelView(topicDTO);
            StringBuilder fillBlankAnswer = new StringBuilder();
            for (FillBlankChoice fillBlankChoice : topicModel.getFillBlankAnswer()){
                fillBlankAnswer.append(fillBlankChoice.getId()).append(".").append(fillBlankChoice.getContent()).append(";");
            }
            topicSubmitModel.setAnswer(fillBlankAnswer.toString());
        }

        topicSubmitModel.setTopicType(topicType);
        return CallResult.success(topicSubmitModel);    }
}
