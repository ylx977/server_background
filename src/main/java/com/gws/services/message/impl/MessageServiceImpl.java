package com.gws.services.message.impl;

import com.gws.entity.CertIdMessage;
import com.gws.repositories.query.mess.CertIdMessageQuery;
import com.gws.repositories.slave.mess.CertIdMessageSlave;
import com.gws.services.message.MessageService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 查询原子的实现类
 * @author : Kumamon 熊本同学
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private CertIdMessageSlave certIdMessageSlave;

    /**
     * 查询全部信息，全表查询
     *
     * @return
     */
    @Override
    public List<CertIdMessage> listCertIdMessage() {

        CertIdMessageQuery query = new CertIdMessageQuery();

        List<CertIdMessage> certIdMessages = certIdMessageSlave.findAll(query);
        return CollectionUtils.isEmpty(certIdMessages) ? Collections.EMPTY_LIST : certIdMessages;
    }

    /**
     * 根据主键查询唯一的一条数据信息
     *
     * @param id
     * @return
     */
    @Override
    public CertIdMessage getCertMessage(Long id) {
        if (null == id){
            return null;
        }
        CertIdMessageQuery query = new CertIdMessageQuery();
        query.setId(id);

        return certIdMessageSlave.findOne(query);
    }
}
