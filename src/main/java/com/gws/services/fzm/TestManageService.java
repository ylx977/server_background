package com.gws.services.fzm;

import com.gws.dto.OperationResult;
import com.gws.dto.test.Year;
import com.gws.entity.CertIdMessage;
import com.gws.entity.DateTest;

import java.util.List;

/**
 * 查询的管理service
 * @author : Kumamon 熊本同学
 * @Description:
 * @Modified By:
 */
public interface TestManageService {

    /**
     * 管理查询类
     * @return
     */
    OperationResult<List<Year>> checkYears();

    /**
     * 证书消息的查询类
     * @return
     */
    OperationResult<List<CertIdMessage>> listCertMessage();

    /**
     * 根据主键查询指定一条的数据信息
     * @param id
     * @return
     */
    OperationResult<CertIdMessage> getCertMessage(Long id);

    /**
     * 保存操作
     * @param dateTest
     * @return
     */
    OperationResult<Boolean> saveDateTest(DateTest dateTest);
}
