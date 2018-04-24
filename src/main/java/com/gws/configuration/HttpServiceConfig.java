
package com.gws.configuration;

import com.gws.utils.webservice.HttpService;
import com.gws.utils.webservice.impl.HttpServiceImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * httpservice配置
 */
@Configuration
@EnableAutoConfiguration
public class HttpServiceConfig {

	@Bean
	public HttpService createHttpService() {
		return new HttpServiceImpl();

	}
}
