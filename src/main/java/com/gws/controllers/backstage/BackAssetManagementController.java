package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.BannerDisplayOrder;
import com.gws.controllers.BaseApiController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.FrontUserBO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.BackAssetManagementService;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/17.
 */
@RestController
@RequestMapping("/api/backstage/assetManagement")
public class BackAssetManagementController extends BaseApiController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackAssetManagementController.class);

    private final HttpServletRequest request;

    private final BackAssetManagementService backAssetManagementService;

    @Autowired
    public BackAssetManagementController(HttpServletRequest request, BackAssetManagementService backAssetManagementService) {
        this.request = request;
        this.backAssetManagementService = backAssetManagementService;
    }

    //===================================以下是前台用户资产模块(用户资产子模块)==================================================
    /**
     * 查询前台资产信息
     * {
     *     "page"
     *     "rowNum"
     *     "uid"
     *     "username"
     *     "email"
     *     "phoneNumber"
     * }
     * @return
     */
    @RequestMapping("/account/queryFrontUserAccount")
    public JsonResult queryFrontUserAccount(@RequestBody FrontUserBO frontUserBO){
        /*
         * 1：前台传入page,rowNum，uid等参数
         * 2：基本参数校验
         * 3：将前台用户的信息根据所给条件查询出来
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询前台用户的资产信息",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(frontUserBO.getPage(),1);
            ValidationUtil.checkMinAndAssignInt(frontUserBO.getRowNum(),1);

        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            PageDTO pageDTO = backAssetManagementService.queryFrontUserAccount(frontUserBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    //===================================以下是前台用户资产模块(充币记录子模块)==================================================

    /**
     * 查询前台用户的充币记录
     * {
     *     "page"
     *     "rowNum"
     *     "coinType"
     *     "uid"
     *     "personName"
     *     "startTime"
     *     "endTime"
     * }
     * @param frontUserBO
     * @return
     */
    @RequestMapping("/topUp/queryFrontUserRecharge")
    public JsonResult queryFrontUserRecharge(@RequestBody FrontUserBO frontUserBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询前台用户的充币记录",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(frontUserBO.getPage(),1);
            ValidationUtil.checkMinAndAssignInt(frontUserBO.getRowNum(),1);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(frontUserBO.getStartTime(), 0);
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(frontUserBO.getEndTime(), Integer.MAX_VALUE);
            frontUserBO.setStartTime(startTime);
            if(startTime > endTime){
                frontUserBO.setEndTime(Integer.MAX_VALUE);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            PageDTO pageDTO = backAssetManagementService.queryFrontUserRecharge(frontUserBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }


    //===================================以下是前台用户资产模块(资产余额子模块)==================================================

    /**
     * 查询平台的资产余额，包括bty和usdg
     * @return
     */
    @RequestMapping("/balance/queryAssetBalance")
    public JsonResult queryAssetBalance(){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询平台的资产余额",uid);

        return null;
    }

    /**
     * 增加平台usdg总量
     * {
     *     "usdg"
     * }
     * @return
     */
    @RequestMapping("/balance/addUsdg")
    public JsonResult addUsdg(){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},增加平台usdg总量",uid);

        return null;
    }

    //===================================以下是前台用户资产模块(提币记录子模块)==================================================


}
