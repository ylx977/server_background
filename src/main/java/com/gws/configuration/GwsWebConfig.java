
package com.gws.configuration;

import com.gws.interceptor.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * spring web 配置
 *
 * @version 
 * @author wangdong
 */
@Configuration
@ComponentScan("com.gws.interceptor")
public class GwsWebConfig extends WebMvcConfigurerAdapter {

	@Value("${spring.corsOrigins}")
	private String corsOrigins;
	/**
	 *
	 * 1、通用日志模块 httpInterceptor
     *
	 * 2、app, sdk拦截
	 * 1) 身份认证模块: uid, channelName, channelId
	 * 2) 公共参数
	 * 3) 解密
	 *
	 * 3、api拦截器 ：
	 * 1) ip白名单
	 *
	 **/
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(httpInterceptor());

		/**api接口**/
		registry.addInterceptor(iPWhitelistInterceptor()).addPathPatterns("/api/**");

	}


	@Bean
	public HandlerInterceptor httpInterceptor() {
		return new HttpInterceptor();
	}

	@Bean
	public HandlerInterceptor iPWhitelistInterceptor() {
		return new IPWhitelistInterceptor();
	}


}
