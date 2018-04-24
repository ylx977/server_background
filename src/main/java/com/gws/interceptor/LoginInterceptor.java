
package com.gws.interceptor;

import com.alibaba.fastjson.JSON;
import com.gws.base.annotation.Anonymous;
import com.gws.controllers.JsonResult;
import com.gws.dto.RequestUser;
import com.gws.enums.SystemCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 【登录拦截器，拦截非登录态的接口调用】
 *
 * @version 4.0.0
 * @author wangdong
 *
 */
@Configuration
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Value("${spring.corsOrigins}")
	private String corsOrigins;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// TODO: 25/04/2017

		if(handler instanceof HandlerMethod){
			HandlerMethod c = (HandlerMethod) handler;
			Anonymous anonymous =c.getMethodAnnotation(Anonymous.class);

			/**允许匿名访问**/
			if (null != anonymous && anonymous.value()) {
			    return true;
			}
			if (null == RequestUser.getCurrentUid()) {
				JsonResult jsonResult = new JsonResult(SystemCode.NEED_LOGIN);
				response.getOutputStream().write(JSON.toJSONString(jsonResult).getBytes());
				response.setContentType("application/json;charset=UTF-8");
				response.setHeader("Access-Control-Allow-Origin", corsOrigins);
				response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
				response.setHeader("Access-Control-Allow-Methods", "POST, GET");
				response.setHeader("Access-Control-Allow-Credentials", "true");

				return false;
			}
			return true;

		}

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

}
