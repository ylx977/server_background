package com.gws.interceptor.backstage;

import com.alibaba.fastjson.JSON;
import com.gws.configuration.backstage.LangConfig;
import com.gws.configuration.backstage.UidConfig;
import com.gws.configuration.backstage.UserInfoConfig;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Component
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityInterceptor.class);

    @Autowired
    private BackUserService backUserService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tokenAndUserId = request.getHeader("Authorization");
        Long uid = ValidationUtil.checkAndAssignLong(tokenAndUserId.split("&")[1]);
        try {
            LOGGER.info("对用户:{},权限进行校验",uid);
            //uri对应的形式是/api/backstage/userManagement/users/createUser（也就是authUrl）
            String requestURI = request.getRequestURI();
            //检查权限
            UserDetailDTO userDetailDTO = backUserService.checkUserAuthority(uid, requestURI);
            //将uid通过request传过去
            request.setAttribute("uid",uid);
            //userDetailDTO通过request传过去
            request.setAttribute("userDetailDTO",userDetailDTO);

            UidConfig.setUid(uid);
            UserInfoConfig.setUserInfo(userDetailDTO);

            return true;
        } catch (Exception e) {
            LOGGER.error("用户:{},无权操作:{}",uid,e.getMessage());
            PrintWriter writer = response.getWriter();
            JsonResult jsonResult = BaseController.authError(e);
            response.setContentType("application/json");
            writer.append(JSON.toJSONString(jsonResult));
            return false;
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoConfig.remove();
        UidConfig.remove();
        System.out.println("移除userDetail和uid");
        super.postHandle(request, response, handler, modelAndView);
    }
}
