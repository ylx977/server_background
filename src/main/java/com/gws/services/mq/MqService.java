package com.gws.services.mq;

/**
 * @author : Kumamon 熊本同学
 * @Description:集成RecketMQservice
 */
public interface MqService {

    /**
     * Mq生产者接口
     */
    String mqProducer(String topic,String tags,String key,String body);

    /**
     * Md订阅者接口
     * @param topic
     * @return
     */
    Boolean mqConsumer(String topic);
}
