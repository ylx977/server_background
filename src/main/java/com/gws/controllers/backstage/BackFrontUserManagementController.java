package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.RegexConstant;
import com.gws.controllers.BaseApiController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.FrontUserBO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.FrontUserService;
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
@RequestMapping("/api/backstage/frontUserManagement")
public class BackFrontUserManagementController extends BaseApiController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackFrontUserManagementController.class);

    private final FrontUserService frontUserService;

    private final HttpServletRequest request;

    @Autowired
    public BackFrontUserManagementController(FrontUserService frontUserService, HttpServletRequest request) {
        this.frontUserService = frontUserService;
        this.request = request;
    }

    //===================================以下是前台用户模块==================================================
    /**
     * 查询前台用户信息
     * {
     *      "page"
     *      "rowNum"
     *      "uid"
     *      "phoneNumber"
     *      "username"
     *      "email"
     *      "startTime"
     *      "endTime"
     * }
     * @return
     */
    @RequestMapping("/users/queryFrontUserInfo")
    public JsonResult queryFrontAccount(@RequestBody FrontUserBO frontUserBO){
        /*
         * 1：前台传入page,rowNum，注册时间，uid，手机，姓名，邮箱等参数
         * 2：基本参数校验
         * 3：将前台用户的信息根据所给条件查询出来
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看前台用户信息",uid);
        try {
            validate(frontUserBO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            PageDTO pageDTO = frontUserService.queryFrontUsers(frontUserBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 修改前台用户的联系方式(手机号码或邮箱地址)
     * {
     *     "uid"
     *     "phoneNumber"
     *     "email"
     * }
     * @param frontUserBO
     * @return
     */
    @RequestMapping("/users/updateFrontUserInfo")
    public JsonResult updateFrontUserInfo(@RequestBody FrontUserBO frontUserBO){
        /*
         * 1：前台传入uid，手机，邮箱等参数
         * 2：基本参数校验
         * 3：将前台用户的手机号或者邮箱地址修改
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},修改前台用户信息",uid);
        try {
            ValidationUtil.checkAndAssignLong(frontUserBO.getUid());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            frontUserService.updateFrontUserInfo(frontUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }

    }

    //===================================以下是前台用户信息修改审核模块==================================================
    /**
     * 查询前台修改申请信息的审核
     * {
     *     "page"
     *     "rowNum"
     *     "uid"
     *     "phoneNumber"
     *     "satrtTime"
     *     "endTime"
     * }
     * @return
     */
    @RequestMapping("/review/queryApplyInfo")
    public JsonResult queryApplyInfo(@RequestBody FrontUserBO frontUserBO){
        /*
         * 1：前台传入page,rowNum，申请时间，uid，手机，姓名，邮箱等参数
         * 2：基本参数校验
         * 3：查询出所有用户的修改申请信息
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看前台用户申请修改审核信息",uid);
        try {
            validate(frontUserBO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            PageDTO pageDTO = frontUserService.queryApplyInfo(frontUserBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 同意用户的申请
     * {
     *     "id"
     * }
     * @return
     */
    @RequestMapping("/review/approveApply")
    public JsonResult approveApply(@RequestBody FrontUserBO frontUserBO){
        /*
         * 1：前台传入申请id号
         * 2：基本参数校验
         * 3：检查该申请数据的状态是否满足要求(否则报错)
         * 4：修改用户申请表中的状态为已经修改
         * 5：再根据用户提交的申请修改用户的信息
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},同意前台用户的修改申请",uid);
        try {
            ValidationUtil.checkAndAssignLong(frontUserBO.getId());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            frontUserService.approveApply(frontUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 拒绝用户的申请
     * {
     *     "id"
     * }
     * @return
     */
    @RequestMapping("/review/rejectApply")
    public JsonResult rejectApply(@RequestBody FrontUserBO frontUserBO){
        /*
         * 1：前台传入申请id号
         * 2：基本参数校验
         * 3：检查该申请数据的状态是否满足要求(否则报错)
         * 4：修改用户申请表中的状态为已经拒绝
         */
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},拒绝前台用户的修改申请",uid);
        try {
            ValidationUtil.checkAndAssignLong(frontUserBO.getId());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessage()+":"+e.getMessage(), null);
        }
        try {
            frontUserService.rejectApply(frontUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessage()+":"+e.getMessage(), null);
        }
    }

    /**
     * 校验的公共方法
     * @param frontUserBO
     */
    private static final void validate(FrontUserBO frontUserBO){
            ValidationUtil.checkMinAndAssignInt(frontUserBO.getPage(),1);
            ValidationUtil.checkMinAndAssignInt(frontUserBO.getRowNum(),1);
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(frontUserBO.getEndTime(),Integer.MAX_VALUE);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(frontUserBO.getStartTime(),0);
            if(startTime > endTime){
                frontUserBO.setEndTime(Integer.MAX_VALUE);
            }else{
                frontUserBO.setEndTime(endTime);
        }
    }

}
