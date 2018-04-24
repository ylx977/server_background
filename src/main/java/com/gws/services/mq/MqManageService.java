package com.gws.services.mq;

import com.gws.dto.OperationResult;

/**
 * @author:wangdong
 * @description:Mq的管理类的接口
 */
public interface MqManageService{

    /**
     * 发布消息
     * @param topic
     * @param tags
     * @param key
     * @param body
     * @return
     */
    OperationResult<String> mqProducer(String topic, String tags, String key, String body);

    /**
     * 订阅消息
     * @param topic
     * @return
     */
    OperationResult<Boolean> mqConsumer(String topic);

}
