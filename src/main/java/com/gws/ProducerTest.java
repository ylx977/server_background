package com.gws;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import java.util.Properties;
/**
 * @author:wangdong
 * @description:
 */

public class ProducerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // 您在 MQ 控制台创建的 Producer ID
        properties.put(PropertyKeyConst.ProducerId, "PID_xiongben01");
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,"LTAIXTrGXmEyge7N");
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "ngyXmTCzKJZkCm6VTWAhJDlglDiahp");
        // 设置 TCP 接入域名（此处以公共云的公网接入为例）
        properties.put(PropertyKeyConst.ONSAddr,
                "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();
        //暂时不需要循环发送消息
        //while(true){
            Message msg = new Message( //
                    // 在控制台创建的 Topic，即该消息所属的 Topic 名称
                    "xiongben",
                    // Message Tag,
                    // 可理解为 Gmail 中的标签，对消息进行再归类，方便 Consumer 指定过滤条件在 MQ 服务器过滤
                    "test01",
                    // Message Body
                    // 任何二进制形式的数据， MQ 不做任何干预，
                    // 需要 Producer 与 Consumer 协商好一致的序列化和反序列化方式
                    "Hello 哈哈哈".getBytes());
            // 设置代表消息的业务关键属性，请尽可能全局唯一，以方便您在无法正常收到消息情况下，可通过 MQ 控制台查询消息并补发
            // 注意：不设置也不会影响消息正常收发
            msg.setKey("TEST_100");
            // 发送消息，只要不抛异常就是成功
            // 打印 Message ID，以便用于消息发送状态查询
            SendResult sendResult = producer.send(msg);
            System.out.println("Send Message success. Message ID is: " + sendResult.getMessageId());
        //}
        // 在应用退出前，可以销毁 Producer 对象
        // 注意：如果不销毁也没有问题
        //producer.shutdown();
    }
}
