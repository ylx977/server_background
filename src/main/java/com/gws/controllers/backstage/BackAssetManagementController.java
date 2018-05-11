package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.BannerDisplayOrder;
import com.gws.controllers.BaseApiController;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.AssetBO;
import com.gws.entity.backstage.AssetBalanceVO;
import com.gws.entity.backstage.BtyAddresses;
import com.gws.entity.backstage.FrontUserBO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.BackAssetManagementService;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/17.
 */
@RestController
@RequestMapping("/api/backstage/assetManagement")
public class BackAssetManagementController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackAssetManagementController.class);

    private final HttpServletRequest request;

    private final BackAssetManagementService backAssetManagementService;

    @Value(value = "${platform.btyAddress}")
    private String platformAddress;

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
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            ValidationUtil.checkMinAndAssignInt(frontUserBO.getPage(),1,lang);
            ValidationUtil.checkMinAndAssignInt(frontUserBO.getRowNum(),1,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            PageDTO pageDTO = backAssetManagementService.queryFrontUserAccount(frontUserBO);
            return success(pageDTO,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
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
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            validate(frontUserBO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            PageDTO pageDTO = backAssetManagementService.queryFrontUserRecharge(frontUserBO);
            return success(pageDTO,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }


    //===================================以下是前台用户资产模块(提币记录子模块)==================================================

    /**
     * 查询前台用户提币的申请信息
     * {
     *     "page"
     *     "rowNum"
     *     "coinType"
     *     "uid"
     *     "personName"
     *     "phoneNumber"
     *     "email"
     *     "startTime"
     *     "endTime"
     *
     * }
     * @return
     */
    @RequestMapping("/withdraw/queryFrontUserWithdraw")
    public JsonResult queryFrontUserWithdraw(@RequestBody FrontUserBO frontUserBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询前台用户提币的申请信息",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            validate(frontUserBO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            PageDTO pageDTO = backAssetManagementService.queryFrontUserWithdraw(frontUserBO);
            return success(pageDTO,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 查询前台用户提币的申请历史信息
     *  {
     *     "page"
     *     "rowNum"
     *     "coinType"
     *     "uid"
     *     "personName"
     *     "phoneNumber"
     *     "email"
     *     "firstChecker"
     *     "SecondChecker"
     *     "startTime"
     *     "endTime"
     *
     * }
     * @return
     */
    @RequestMapping("/withdraw/queryFrontUserWithdrawHistory")
    public JsonResult queryFrontUserWithdrawHistory(@RequestBody FrontUserBO frontUserBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询前台用户提币的申请历史信息",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            validate(frontUserBO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            PageDTO pageDTO = backAssetManagementService.queryFrontUserWithdrawHistory(frontUserBO);
            return success(pageDTO,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 初审同意前台用户的提币申请
     * {
     *     "id"
     * }
     * @return
     */
    @RequestMapping("/withdraw/firstPass")
    public JsonResult firstPass(@RequestBody FrontUserBO frontUserBO){
        Long uid = (Long) request.getAttribute("uid");
        UserDetailDTO userDetailDTO = (UserDetailDTO) request.getAttribute("userDetailDTO");
        LOGGER.info("用户:{},初审同意前台用户的提币申请",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            ValidationUtil.checkAndAssignLong(frontUserBO.getId(),lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            frontUserBO.setUserDetailDTO(userDetailDTO);
            backAssetManagementService.firstPass(frontUserBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 复审同意前台用户的提币申请
     * {
     *     "id"
     * }
     * @return
     */
    @RequestMapping("/withdraw/secondPass")
    public JsonResult secondPass(@RequestBody FrontUserBO frontUserBO){
        Long uid = (Long) request.getAttribute("uid");
        UserDetailDTO userDetailDTO = (UserDetailDTO) request.getAttribute("userDetailDTO");
        LOGGER.info("用户:{},复审同意前台用户的提币申请",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            ValidationUtil.checkAndAssignLong(frontUserBO.getId(),lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            frontUserBO.setUserDetailDTO(userDetailDTO);
            backAssetManagementService.secondPass(frontUserBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 拒绝前台用户的提币申请
     * {
     *     "id"
     * }
     * @return
     */
    @RequestMapping("/withdraw/rejectWithdraw")
    public JsonResult rejectWithdraw(@RequestBody FrontUserBO frontUserBO){
        Long uid = (Long) request.getAttribute("uid");
        UserDetailDTO userDetailDTO = (UserDetailDTO) request.getAttribute("userDetailDTO");
        LOGGER.info("用户:{},拒绝前台用户的提币申请",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            ValidationUtil.checkAndAssignLong(frontUserBO.getId(),lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            frontUserBO.setUserDetailDTO(userDetailDTO);
            backAssetManagementService.rejectWithdraw(frontUserBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }


    //===================================以下是前台用户资产模块(兑换/挂单子模块)==================================================


    /**
     * 查询平台兑换信息（也就是查询平台的即时交易信息）
     * {
     *     "page"
     *     "rowNum"
     *     "tradeType"
     *     "uid"
     *     "personName"
     *     "phoneNumber"
     *     "email"
     *     "startTime"
     *     "endTime"
     * }
     * @return
     */
    @RequestMapping("/exchange/queryExchange")
    public JsonResult queryExchange(@RequestBody FrontUserBO frontUserBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询平台兑换信息",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            validate(frontUserBO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            PageDTO pageDTO = backAssetManagementService.queryExchange(frontUserBO);
            return success(pageDTO,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }


    //===================================以下是前台用户资产模块(资产余额子模块)==================================================

    /**
     * 查询平台的资产余额，包括bty和usdg
     * 不用传任何参数
     * @return
     */
    @RequestMapping("/balance/queryAssetBalance")
    public JsonResult queryAssetBalance(@RequestBody FrontUserBO frontUserBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查询平台的资产余额",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        frontUserBO.setLang(lang);
        try {
            AssetBalanceVO assetBalanceVO = backAssetManagementService.queryAssetBalance();
            return success(assetBalanceVO,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 增加平台usdg总量(同时增加平台的黄金总量，前台黄金是以kg的单位形式过来的，后台数据库以g为单位保存黄金库存量)（1g 黄金 = 100 USDG）
     * {
     *     "gold"
     * }
     * @return
     */
    @RequestMapping("/balance/addUsdg")
    public JsonResult addUsdg(@RequestBody AssetBO assetBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},增加平台usdg总量",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        assetBO.setLang(lang);
        try {
            ValidationUtil.checkMinAndAssignDouble(assetBO.getGold(),0,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            backAssetManagementService.addUsdg(assetBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 点击充币显示平台的比特元地址
     * @return
     */
    @RequestMapping("/balance/queryPlatformAddress")
    public JsonResult queryPlatformAddress(){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看平台的bty地址",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        return success(platformAddress,lang);
    }

    /**
     * 点击地址管理显示平台提币管理的所有地址
     * @return
     */
    @RequestMapping("/balance/queryAddresses")
    public JsonResult queryAddresses(@RequestBody AssetBO assetBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看平台地址管理的所有地址",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        assetBO.setLang(lang);
        try {
            List<BtyAddresses> btyAddressesList = backAssetManagementService.queryAddresses();
            return success(btyAddressesList,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 删除管理的地址
     * {
     *     "id"
     * }
     * @return
     */
    @RequestMapping("/balance/deleteAddress")
    public JsonResult deleteAddresses(@RequestBody AssetBO assetBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},删除平台管理的地址",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        assetBO.setLang(lang);
        try {
            ValidationUtil.checkAndAssignLong(assetBO.getId(),lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            backAssetManagementService.deleteAddresses(assetBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 添加管理的地址
     * {
     *     "address"
     *     "tag"
     * }
     * @return
     */
    @RequestMapping("/balance/addAddress")
    public JsonResult addAddresses(@RequestBody AssetBO assetBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},添加平台管理的地址",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        try {
            ValidationUtil.checkBlankAndAssignString(assetBO.getAddress(),lang);
            ValidationUtil.checkBlankAndAssignString(assetBO.getTag(),lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            backAssetManagementService.addAddresses(assetBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 设置管理的默认地址
     * {
     *     id
     * }
     * @return
     */
    @RequestMapping("/balance/setDefaultAddress")
    public JsonResult setDefaultAddress(@RequestBody AssetBO assetBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},设置管理的默认地址",uid);
        Integer lang = (Integer) request.getAttribute("lang");
        assetBO.setLang(lang);
        try {
            ValidationUtil.checkAndAssignLong(assetBO.getId(),lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e,lang);
        }
        try {
            backAssetManagementService.setDefaultAddress(assetBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e,lang);
        }
    }





    //===================================通用方法==================================================
    /**
     * 查询校验的通用方法
     * @param frontUserBO
     */
    private static void validate(FrontUserBO frontUserBO){
        Integer lang = frontUserBO.getLang();
        ValidationUtil.checkMinAndAssignInt(frontUserBO.getPage(),1,lang);
        ValidationUtil.checkMinAndAssignInt(frontUserBO.getRowNum(),1,lang);
        Integer startTime = ValidationUtil.checkAndAssignDefaultInt(frontUserBO.getStartTime(),lang, 0);
        Integer endTime = ValidationUtil.checkAndAssignDefaultInt(frontUserBO.getEndTime(), Integer.MAX_VALUE,lang);
        frontUserBO.setStartTime(startTime);
        if(startTime > endTime){
            frontUserBO.setEndTime(Integer.MAX_VALUE);
        }
    }



}
