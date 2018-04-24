package com.gws.controllers.backstage;

import com.gws.controllers.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/17.
 */
@RestController
@RequestMapping("/api/backstage/assetManagement")
public class BackAssetManagementController {

    //===================================以下是前台用户资产模块信息==================================================
    /**
     * 查询前台资产信息
     * @return
     */
    @RequestMapping("/account/queryFrontUserAccount")
    public JsonResult queryFrontUserAccount(){
        /*
         * 1：前台传入page,rowNum，uid等参数
         * 2：基本参数校验
         * 3：将前台用户的信息根据所给条件查询出来
         */
        return null;
    }


}
