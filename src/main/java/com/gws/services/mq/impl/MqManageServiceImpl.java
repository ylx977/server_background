package com.gws.services.mq.impl;

import com.gws.dto.OperationResult;
import com.gws.enums.BizErrorCode;
import com.gws.services.mq.MqManageService;
import com.gws.services.mq.MqService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:wangdong
 * @description:
 */
@Service
public class MqManageServiceImpl implements MqManageService {

    @Autowired
    private MqService mqService;


    /**
     * 发布消息
     *
     * @param topic
     * @param tags
     * @param key
     * @param body
     * @return
     */
    @Override
    public OperationResult<String> mqProducer(String topic, String tags, String key, String body) {
        if (StringUtils.isEmpty(topic) || StringUtils.isEmpty(tags) || StringUtils.isEmpty(body)){
            return new OperationResult<>(BizErrorCode.PARM_ERROR);
        }
        String messageId = mqService.mqProducer(topic,tags,key,body);
        if (StringUtils.isNotEmpty(messageId)){
            return new OperationResult<>(messageId);
        }

        return new OperationResult<>(BizErrorCode.SEND_FAIL);

    }

    /**
     * 订阅消息
     *
     * @param topic
     * @return
     */
    @Override
    public OperationResult<Boolean> mqConsumer(String topic) {
        if (StringUtils.isEmpty(topic)){
            return new OperationResult<>(BizErrorCode.PARM_ERROR);
        }

        Boolean result = mqService.mqConsumer(topic);
        if (result){
            return new OperationResult<>(true);
        }

        return new OperationResult<>(false);
    }


}
