package com.gws.services.mq.impl;

import com.gws.dto.OperationResult;
import com.gws.services.mq.MqManageService;
import org.springframework.stereotype.Service;

/**
 * @author:wangdong
 * @description:Mq的管理类的接口
 */
@Service
public class MqManageServiceImpl implements MqManageService{
    @Override
    public OperationResult<String> mqProducer(String topic, String tags, String key, String body) {
        return null;
    }

    @Override
    public OperationResult<Boolean> mqConsumer(String topic) {
        return null;
    }

}
