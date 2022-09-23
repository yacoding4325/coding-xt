package com.coding.xt.admin.controller;

import com.alibaba.fastjson.JSON;
import com.coding.xt.admin.model.TopicModel;
import com.coding.xt.admin.params.TopicParam;
import com.coding.xt.admin.service.TopicService;
import com.coding.xt.common.enums.TopicType;
import com.coding.xt.common.model.CallResult;
import com.coding.xt.common.model.topic.ContentAndImage;
import com.coding.xt.common.utils.POIUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author yaCoding
 * @create 2022-09-22 下午 8:59
 */
@RestController
@RequestMapping("topic")
@Slf4j
public class AdminTopicController {

    @Autowired
    private TopicService topicService;


    @RequestMapping(value = "findPage")
    public CallResult findPage(@RequestBody TopicParam topicParam){
        return topicService.findTopicList(topicParam);
    }


    @RequestMapping("uploadExcel/{subjectId}")
    public CallResult upload(@RequestParam("excelFile") MultipartFile multipartFile,
                             @PathVariable("subjectId") Long subjectId){
        int updateNum = 0;
        if (subjectId == -1){
            return CallResult.fail(-999,"subjectId 不能为空");
        }
        try {
            List<String[]> list = POIUtils.readExcel(multipartFile);
            for (String[] strs : list){
                if (StringUtils.isEmpty(strs[0])){
                    continue;
                }
                Integer subjectUnit = Integer.parseInt(strs[0]);
                if (StringUtils.isEmpty(strs[1])){
                    continue;
                }
                Integer topicType = Integer.parseInt(strs[1]);

                String topicTitle = strs[2];
                List<Map<String, ContentAndImage>> choiceList = new ArrayList<>();
                String choiceA = strs[3];
                ContentAndImage a = ContentAndImage.deal(choiceA);
                if (a != null){
                    Map<String,ContentAndImage> aMap = new HashMap<>();
                    aMap.put("A",a);
                    choiceList.add(aMap);
                }
                String choiceB = strs[4];
                ContentAndImage b = ContentAndImage.deal(choiceB);
                if (b != null){
                    Map<String,ContentAndImage> bMap = new HashMap<>();
                    bMap.put("B",b);
                    choiceList.add(bMap);
                }
                String choiceC = strs[5];
                ContentAndImage c = ContentAndImage.deal(choiceC);
                if (c != null){
                    Map<String,ContentAndImage> cMap = new HashMap<>();
                    cMap.put("C",c);
                    choiceList.add(cMap);
                }
                String choiceD = strs[6];
                ContentAndImage d = ContentAndImage.deal(choiceD);
                if (d != null){
                    Map<String,ContentAndImage> dMap = new HashMap<>();
                    dMap.put("D",d);
                    choiceList.add(dMap);
                }
                String choiceE = strs[7];
                ContentAndImage e = ContentAndImage.deal(choiceE);
                if (e != null){
                    Map<String,ContentAndImage> eMap = new HashMap<>();
                    eMap.put("E",e);
                    choiceList.add(eMap);
                }
                String choiceF = "";
                if (strs.length >= 9) {
                    choiceF = strs[8];
                }
                ContentAndImage f = ContentAndImage.deal(choiceF);
                if (f != null){
                    Map<String,ContentAndImage> fMap = new HashMap<>();
                    fMap.put("F",f);
                    choiceList.add(fMap);
                }
                String choiceG = "";
                if (strs.length >= 10) {
                    choiceG = strs[9];
                }
                ContentAndImage g = ContentAndImage.deal(choiceG);
                if (g != null){
                    Map<String,ContentAndImage> gMap = new HashMap<>();
                    gMap.put("G",g);
                    choiceList.add(gMap);
                }
                String choiceH = "";
                if (strs.length >= 11) {
                    choiceH = strs[10];
                }
                ContentAndImage h = ContentAndImage.deal(choiceH);
                if (h != null){
                    Map<String,ContentAndImage> hMap = new HashMap<>();
                    hMap.put("H",h);
                    choiceList.add(hMap);
                }
                String topicAnswer = "";
                if (strs.length >= 12) {
                    topicAnswer = strs[11];
                }
                String topicAnalyze = "";
                if (strs.length >= 13) {
                    topicAnalyze = strs[12];
                }
                Integer topicStar = 3;
                if (strs.length >= 14) {
                    if (!StringUtils.isEmpty(strs[13])) {
                        topicStar = Integer.parseInt(strs[13]);
                    }
                }
                String topicAreaCity = "";
                if (strs.length >= 15) {
                    topicAreaCity = strs[14];
                }
                String topicImage = null;
                if (strs.length >= 16) {
                    topicImage = strs[15];
                }
                List<String> topicImageList = new ArrayList<>();
                //多个图片 [http://xxx.png,http://nnn.png]
                if (!StringUtils.isEmpty(topicImage)) {
                    Pattern pt = Pattern.compile("http?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
                    Matcher mt = pt.matcher(topicImage);
                    if (mt.find()) {
                        String imagesStr = mt.group();
                        String[] images = imagesStr.split(",");
                        topicImageList.addAll(Arrays.asList(images));
                    }
                }
                TopicParam topic = new TopicParam();
                topic.setTopicTitle(topicTitle);
                topic.setTopicType(topicType);
                topic.setSubjectUnit(subjectUnit);
                topic.setTopicAnalyze(topicAnalyze);
                if (TopicType.FILL_BLANK.getCode() == topicType){
                    String[] strAnswer = topicAnswer.split("\\$;\\$");
                    List<Map<String,Object>> map = new ArrayList<>();
                    for (int i = 1;i<=strAnswer.length;i++){
                        Map<String,Object> m = new HashMap<>();
                        m.put("id",i);
                        m.put("content",strAnswer[i-1]);
                        map.add(m);
                    }
                    topic.setTopicChoice(JSON.toJSONString(map));
                }else{
                    topic.setTopicChoice(JSON.toJSONString(choiceList));
                }
                topic.setTopicAnswer(topicAnswer);

                topic.setCreateTime(System.currentTimeMillis());
                topic.setLastUpdateTime(System.currentTimeMillis());
                topic.setTopicStar(topicStar);
                topic.setTopicAreaCity(topicAreaCity);
                topic.setTopicImg(JSON.toJSONString(topicImageList));
                topic.setAddAdmin("admin");
                topic.setTopicAreaPro("");
                topic.setTopicSubject(subjectId);

                TopicModel t = this.topicService.findTopicByTitle(topic.getTopicTitle());
                if (t != null){
                    log.info("update topic:{}",topic.getTopicTitle());
                    topic.setId(t.getId());
                    updateNum++;
                    this.topicService.updateTopic(topic);
                }else{
                    this.topicService.saveTopic(topic);
                }
//
            }
//            System.out.println(JSON.toJSONString(list));
        } catch (IOException e) {
            e.printStackTrace();
            return CallResult.fail();
        }
        log.info("update num:{}",updateNum);
        return CallResult.success();
    }
}
