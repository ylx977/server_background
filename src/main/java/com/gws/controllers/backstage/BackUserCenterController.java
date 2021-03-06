package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RegexConstant;
import com.gws.configuration.backstage.UidConfig;
import com.gws.configuration.backstage.UserInfoConfig;
import com.gws.controllers.BaseApiController;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.BackUserBO;
import com.gws.enums.SystemCode;
import com.gws.exception.ExceptionUtils;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.validate.ValidationUtil;
import com.gws.utils.webservice.HashUtil;
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
@RequestMapping("/back/api/backstage/userCenter")
public class BackUserCenterController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackUserCenterController.class);

    private final BackUserService backUserService;
    private final HttpServletRequest request;

    @Autowired
    public BackUserCenterController(BackUserService backUserService, HttpServletRequest request) {
        this.backUserService = backUserService;
        this.request = request;
    }

    /**
     * 后台用户自己修改自己的密码
     * {
     *     "originalPassword"
     *     "newPassword"
     *     "newConfirmedPassword"
     * }
     * @return
     */
    @RequestMapping("/modifyPassword")
    public JsonResult modifyPassword(@RequestBody BackUserBO backUserBO){
        /*
         * 1：前端传过来原始密码，新密码，确认的新密码和uid参数
         * 2：基本参数校验
         * 3：修改后台用户的账号密码
         */
        Long uid = UidConfig.getUid();
        UserDetailDTO userDetailDTO = UserInfoConfig.getUserInfo();
        LOGGER.info("用户:{},后台用户修改自己密码",uid);
        try {
            String password = userDetailDTO.getPassword();
            String originalPassword = ValidationUtil.checkBlankAndAssignString(backUserBO.getOriginalPassword(), RegexConstant.PWD_REGEX);
            String newPassword = ValidationUtil.checkBlankAndAssignString(backUserBO.getNewPassword(), RegexConstant.PWD_REGEX);
            String newConfirmedPassword = ValidationUtil.checkBlankAndAssignString(backUserBO.getNewConfirmedPassword(), RegexConstant.PWD_REGEX);
            if(!password.equals(HashUtil.hashPwd(originalPassword))){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.WRONG_PASSWORD));
            }
            if(!newPassword.equals(newConfirmedPassword)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.PASSWORD_INCONSISTENT));
            }
            if(password.equals(newPassword)){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.SAME_PASSWORD));
            }
            backUserBO.setUid(uid);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backUserService.modifyPassword(backUserBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

}
