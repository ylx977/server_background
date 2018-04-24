package com.gws.services.mq.impl;

import com.aliyun.openservices.ons.api.*;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import com.gws.GwsWebApplication;
import com.gws.services.mq.MqService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author:wangdong
 * @description:Mq的实现类
 */
@Configuration
@Service
public class MqServiceImpl implements MqService {

    static Logger logger = Logger.getLogger(GwsWebApplication.class);

    @Value("${mq.producerId}")
    private String mqProducerId;

    @Value("${mq.consumerId}")
    private String mqConsumerId;

    @Value("${ali.accessKey}")
    private String aliAccessKey;

    @Value("${ali.secretKey}")
    private String aliSecretKey;

    @Value("${mq.tcpHost}")
    private String mqTcpHost;

    Properties properties = new Properties();

    @PostConstruct
    public void init() {
        logger.info("mqInit Started");
        //初始化参数
        properties.put(PropertyKeyConst.AccessKey, aliAccessKey);
        properties.put(PropertyKeyConst.SecretKey, aliSecretKey);
        properties.put(PropertyKeyConst.ONSAddr,
                mqTcpHost);
    }



    /**
     * Mq生产者接口
     *
     * @param topic
     * @param tags
     * @param key
     * @param body
     */
    @Override
    public String mqProducer(String topic, String tags, String key, String body) {
        if (StringUtils.isEmpty(topic) || StringUtils.isEmpty(tags) || StringUtils.isEmpty(body)){
            return null;
        }
        properties.put(PropertyKeyConst.ProducerId, mqProducerId);
        Producer producer = ONSFactory.createProducer(properties);
        producer.start();
        Message msg = new Message(topic,tags,body.getBytes());
        msg.setKey(key);
        // 发送消息，只要不抛异常就是成功
        // 打印 Message ID，以便用于消息发送状态查询
        SendResult sendResult = new SendResult();
        try {
            sendResult = producer.send(msg);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (StringUtils.isNotEmpty(sendResult.getMessageId())){
            System.out.println("Send Message success. Message ID is: " + sendResult.getMessageId());
            return sendResult.getMessageId();
        }
        return null;
    }

    /**
     * Mq订阅者接口
     *
     * @param topic
     * @return
     */
    @Override
    public Boolean mqConsumer(String topic) {
        if (StringUtils.isEmpty(topic)){
            return false;
        }
        properties.put(PropertyKeyConst.ConsumerId, mqConsumerId);
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(topic, "*", new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                return Action.CommitMessage;
            }
        });

        try {
            consumer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Consumer" + topic + "Started");
        return true;
    }


}
