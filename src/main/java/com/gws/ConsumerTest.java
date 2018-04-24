package com.gws;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import java.util.Properties;
/**
 * @author:wangdong
 * @description:订阅者
 */
public class ConsumerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // 您在 MQ 控制台创建的 Consumer ID
        properties.put(PropertyKeyConst.ConsumerId, "CID_wangdong01");
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, "LTAIXTrGXmEyge7N");
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "ngyXmTCzKJZkCm6VTWAhJDlglDiahp");
        // 设置 TCP 接入域名（此处以公共云公网环境接入为例）
        properties.put(PropertyKeyConst.ONSAddr,
                "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("xiongben", "*", new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer Started");
    }
}
