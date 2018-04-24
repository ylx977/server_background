package com.gws.services.fzm;

import com.gws.dto.OperationResult;
import com.gws.dto.test.Year;
import com.gws.entity.DateTest;

import java.util.List;

/**
 * 查询的原子类的service
 * @author : Kumamon 熊本同学
 * @Description:
 * @Modified By:
 */
public interface TestService {

    /**
     * 查询
     * @return
     */
    List<DateTest> listDateTest();

    /**
     * 保存的操作
     * @param dateTest
     * @return
     */
    DateTest saveDateTest(DateTest dateTest);
}
