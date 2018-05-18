package com.gws.interceptor.backstage;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.BackUserStatus;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author ylx
 * 检查用户的状态是否处于冻结状态，同时检测是否已经被删除了
 * Created by fuzamei on 2018/5/11.
 */
@Component
public class FreezeInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreezeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tokenAndUserId = request.getHeader("Authorization");
        Integer lang = (Integer)(request.getAttribute("lang"));
        Long uid = ValidationUtil.checkAndAssignLong(tokenAndUserId.split("&")[1],lang);
        UserDetailDTO userDetailDTO = (UserDetailDTO) request.getAttribute("userDetailDTO");
        LOGGER.info("对用户的冻结状态和删除状态进行检查");
        //检查是否是冻结状态
        if(userDetailDTO.getIsFreezed().equals(BackUserStatus.FREEZED)){
            LOGGER.error("用户:{},当前用户已经被冻结",uid);
            PrintWriter writer = response.getWriter();
            JsonResult jsonResult = BaseController.freezeFail(lang);
            response.setContentType("application/json");
            writer.append(JSON.toJSONString(jsonResult));
            return false;
        }
        //检查是否是删除状态
        if(userDetailDTO.getIsDelete().equals(BackUserStatus.DELETED)){
            LOGGER.error("用户:{},当前用户已经被删除",uid);
            PrintWriter writer = response.getWriter();
            JsonResult jsonResult = BaseController.deleteFail(lang);
            response.setContentType("application/json");
            writer.append(JSON.toJSONString(jsonResult));
            return false;
        }
        return true;
    }

}
