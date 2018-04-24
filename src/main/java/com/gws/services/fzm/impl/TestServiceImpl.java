package com.gws.services.fzm.impl;

import com.gws.entity.DateTest;
import com.gws.repositories.master.test.DateTestMaster;
import com.gws.repositories.query.test.DateTestQuery;
import com.gws.repositories.slave.test.DateTestSlave;
import com.gws.services.fzm.TestService;
import com.gws.utils.cache.IdGlobalGenerator;
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
public class TestServiceImpl implements TestService {

    @Autowired
    private IdGlobalGenerator idGen;

    @Autowired
    private DateTestSlave dateTestSlave;

    @Autowired
    private DateTestMaster dateTestMaster;

    @Override
    public List<DateTest> listDateTest() {

        DateTestQuery query = new DateTestQuery();
        List<DateTest> dateTests = dateTestSlave.findAll(query);

        return CollectionUtils.isEmpty(dateTests) ? Collections.EMPTY_LIST : dateTests;
    }

    /**
     * 保存的操作
     *
     * @param dateTest
     * @return
     */
    @Override
    public DateTest saveDateTest(DateTest dateTest) {
        if (null == dateTest){
            return null;
        }

        if (null == dateTest.getId()) {
            dateTest.setId(idGen.getSeqId(DateTest.class).intValue());
        }

        return dateTestMaster.saveAndFlush(dateTest);
    }
}
