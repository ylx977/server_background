package com.gws.controllers.api;

import com.gws.controllers.BaseApiController;
import com.gws.controllers.JsonResult;
import com.gws.dto.OperationResult;
import com.gws.dto.test.Year;
import com.gws.entity.CertIdMessage;
import com.gws.entity.DateTest;
import com.gws.services.fzm.TestManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * test接口
 * @author : Kumamon 熊本同学
 * @Description:
 * @Modified By:
 */
@RestController
@RequestMapping("/api/fzm/")
public class TestController extends BaseApiController {

    @Autowired
    private TestManageService testManageService;

    /**
     * 练习题
     * @return
     */
    @RequestMapping("checkYears")
    public JsonResult checkYears(){

        OperationResult<List<Year>> result = testManageService.checkYears();

        if (result.getSucc()){
            return success(result.getEntity());
        }

        return error(result.getErrorCode());
    }

    /**
     * 练习题
     * @return
     */
    @RequestMapping("saveDateTest")
    public JsonResult saveDateTest(DateTest dateTest){

        OperationResult<Boolean> result = testManageService.saveDateTest(dateTest);

        if (result.getSucc()){
            return success(result.getEntity());
        }

        return error(result.getErrorCode());
    }

    /**
     * 查询消息的接口
     * @return
     */
    @RequestMapping("listCertMessage")
    public JsonResult listCertMessage(){

        OperationResult<List<CertIdMessage>> result = testManageService.listCertMessage();

        if (result.getSucc()){
            return success(result.getEntity());
        }

        return error(result.getErrorCode());
    }

    /**
     * 根据指定参数查询数据
     * @return
     */
    @RequestMapping("getCertMessage")
    public JsonResult getCertMessage(Long id){

        OperationResult<CertIdMessage> result = testManageService.getCertMessage(id);

        if (result.getSucc()){
            return success(result.getEntity());
        }

        return error(result.getErrorCode());
    }
}
