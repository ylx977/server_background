package com.gws.controllers.api;

import com.gws.controllers.BaseApiController;
import com.gws.controllers.JsonResult;
import com.gws.dto.OperationResult;
import com.gws.services.mq.MqManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:wangdong
 * @description:集成RecketMq的接口
 */
@RestController
@RequestMapping("/api/mq/")
public class MqController extends BaseApiController {

    @Autowired
    private MqManageService mqManageService;

    /**
     * 消息发布者
     * @param topic
     * @param tags
     * @param key
     * @param body
     * @return
     */
    @RequestMapping("mqProducer")
    public JsonResult mqProducer(String topic, String tags, String key, String body){

        OperationResult<String> result = mqManageService.mqProducer(topic,tags,key,body);

        if (result.getSucc()){
            return success(result.getEntity());
        }
        return error(result.getErrorCode().getCode(),result.getErrorCode().getMessage());
    }

    @RequestMapping("mqConsumer")
    public JsonResult mqConsumer(String topic){

        OperationResult<Boolean> result = mqManageService.mqConsumer(topic);

        if (result.getSucc()){
            return success(result.getEntity());
        }
        return error(result.getErrorCode().getCode(),result.getErrorCode().getMessage());
    }

}
