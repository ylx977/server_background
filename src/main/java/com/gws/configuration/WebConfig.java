package com.gws.configuration;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.gws.utils.json.JsonParameterBinder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// equivalent to <mvc:argument-resolvers>
		argumentResolvers.add(0, new JsonParameterBinder());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// equivalent to <mvc:message-converters>
		converters.add(0, new FastJsonHttpMessageConverter());
	}
	
}