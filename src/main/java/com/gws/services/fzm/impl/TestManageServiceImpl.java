package com.gws.services.fzm.impl;

import com.gws.dto.OperationResult;
import com.gws.dto.test.Year;
import com.gws.entity.CertIdMessage;
import com.gws.entity.DateTest;
import com.gws.enums.date.MonthEnum;
import com.gws.services.fzm.TestManageService;
import com.gws.services.fzm.TestService;
import com.gws.services.message.MessageService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询管理的实现类
 * @author : Kumamon 熊本同学
 */
@Service
public class TestManageServiceImpl implements TestManageService {

    @Autowired
    private TestService testService;

    @Autowired
    private MessageService messageService;

    @Override
    public OperationResult<List<Year>> checkYears() {

        List<Year> years = new ArrayList<>();
        List<DateTest> dateTests = testService.listDateTest();
        if (CollectionUtils.isEmpty(dateTests)){
            return new OperationResult<>(years);
        }

        //先把年份取出来
        List<Integer> yearList = new ArrayList<>();
        dateTests.forEach(dateTest -> {
            yearList.add(dateTest.getYear());
        });
        //将年份去重,得到去重后的年份
        List<Integer> distinctYears = yearList.stream().distinct().sorted().collect(Collectors.toList());

       List<Year> yearArrayList = new ArrayList<>();
       distinctYears.forEach(distinctYear ->{
           Year year = new Year();
           year.setYear(distinctYear);
           yearArrayList.add(year);
       });

       dateTests.forEach(dateTest -> {
               yearArrayList.forEach(year -> {
               if (dateTest.getYear().equals(year.getYear())){
                   if (dateTest.getMonth().equals(MonthEnum.M1.getCode())){
                       year.setM1Amount(dateTest.getAmount());
                   }else if (dateTest.getMonth().equals(MonthEnum.M2.getCode())){
                       year.setM2Amount(dateTest.getAmount());
                   }else if (dateTest.getMonth().equals(MonthEnum.M3.getCode())){
                       year.setM3Amount(dateTest.getAmount());
                   }else if(dateTest.getMonth().equals(MonthEnum.M4.getCode())){
                       year.setM4Amount(dateTest.getAmount());
                   }else {
                       year.setM5Amount(dateTest.getAmount());
                   }
               }
           });
       });

        return new OperationResult<>(yearArrayList);
    }

    /**
     * 证书消息的查询类
     *
     * @return
     */
    @Override
    public OperationResult<List<CertIdMessage>> listCertMessage() {

        List<CertIdMessage> certIdMessages = messageService.listCertIdMessage();

        return new OperationResult<>(certIdMessages);
    }

    /**
     * 根据主键查询指定一条的数据信息
     *
     * @param id
     * @return
     */
    @Override
    public OperationResult<CertIdMessage> getCertMessage(Long id) {

        CertIdMessage certIdMessage = messageService.getCertMessage(id);

        return new OperationResult<>(certIdMessage);
    }

    /**
     * 保存操作
     *
     * @param dateTest
     * @return
     */
    @Override
    public OperationResult<Boolean> saveDateTest(DateTest dateTest) {

        DateTest date = testService.saveDateTest(dateTest);
        if (null != date){
            return new OperationResult<>(true);
        }

        return new OperationResult<>(false);
    }
}
