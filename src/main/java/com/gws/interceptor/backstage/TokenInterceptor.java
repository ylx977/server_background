package com.gws.interceptor.backstage;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.HintMessage;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.enums.SystemCode;
import com.gws.exception.ExceptionUtils;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenInterceptor.class);

    @Autowired
    private BackUserService backUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer lang = ValidationUtil.getInteger(request.getHeader("lang"),1);
        request.setAttribute("lang",lang);
        try {
            LOGGER.info("对用户的token信息进行校验");
            String tokenAndUserId = request.getHeader("Authorization");
            //请求头不能为null
            if(tokenAndUserId == null||"".equals(tokenAndUserId.trim())){
                ExceptionUtils.throwException(ErrorMsg.NULL_AUTH,lang);
            }
            //token校验
            String token = ValidationUtil.checkBlankAndAssignString(tokenAndUserId.split("&")[0].replace("Bearer", ""),lang);
            //账户id校验
            Long uid = ValidationUtil.checkAndAssignLong(tokenAndUserId.split("&")[1],lang);
            //校验userId和token值是否符合
            boolean flag = backUserService.verificationToken(uid, token);
            //token校验未通过
            if(!flag){
                ExceptionUtils.throwException(ErrorMsg.TOKEN_FAIL,lang);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("拦截token信息发生异常:{}",e.getMessage());
            PrintWriter writer = response.getWriter();
            JsonResult jsonResult = BaseController.tokenError(e,lang);
            response.setContentType("application/json");
            writer.append(JSON.toJSONString(jsonResult));
            return false;
        }
    }
}
