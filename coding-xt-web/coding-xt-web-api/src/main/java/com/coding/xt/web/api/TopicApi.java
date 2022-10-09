package com.coding.xt.web.api;

import com.coding.xt.common.model.CallResult;
import com.coding.xt.web.service.TopicService;
import com.coding.xt.web.model.params.TopicParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:03
 */
@RestController
@RequestMapping("topic")
public class TopicApi {

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "practice",method = RequestMethod.POST)
    public CallResult practice(@RequestBody TopicParam topicParam){
        return topicService.practice(topicParam);
    }

    @PostMapping("submit")
    public CallResult submit(@RequestBody TopicParam topicParam){
        return topicService.submit(topicParam);
    }

    @PostMapping("jump")
    public CallResult jump(@RequestBody TopicParam topicParam){
        return topicService.jump(topicParam);
    }

    //练习--实践历史
    @PostMapping(value = "practiceHistory")
    public CallResult practiceHistory(@RequestBody TopicParam topicParam){
        return topicService.practiceHistory(topicParam);
    }

    //用户问题查询
    @PostMapping(value = "userProblemSearch")
    public CallResult userProblemSearch(@RequestBody TopicParam topicParam){
        return topicService.userProblemSearch(topicParam);
    }

}

