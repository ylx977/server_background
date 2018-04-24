package com.gws.services.message;

import com.gws.entity.CertIdMessage;
import com.gws.entity.DateTest;

import java.util.List;

/**
 * 消息查询的原子类的service
 * @author : Kumamon 熊本同学
 * @Description:
 * @Modified By:
 */
public interface MessageService {

    /**
     * 查询全部信息，全表查询
     * @return
     */
    List<CertIdMessage> listCertIdMessage();

    /**
     * 根据主键查询唯一的一条数据信息
     * @param id
     * @return
     */
    CertIdMessage getCertMessage(Long id);
}
