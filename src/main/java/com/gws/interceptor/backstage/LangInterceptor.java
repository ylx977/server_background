package com.gws.interceptor.backstage;

import com.gws.configuration.backstage.LangConfig;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ylx
 * 支持多国语言的拦截器
 * Created by fuzamei on 2018/5/11.
 */
@Component
public class LangInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LangInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer lang = ValidationUtil.getInteger(request.getHeader("lang"), 1);
        LangConfig.setLang(lang);
        request.setAttribute("lang",lang);
        System.out.println("lang拦截器"+lang);
        //统统放过
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LangConfig.remove();
        System.out.println("移除lang");
        super.postHandle(request, response, handler, modelAndView);
    }
}
