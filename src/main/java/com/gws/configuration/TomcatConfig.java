/*
 * Copyright (C) 2016  HangZhou YuShi Technology Co.Ltd  Holdings Ltd. All rights reserved
 *
 * 本代码版权归杭州宇石科技所有，且受到相关的法律保护。
 * 没有经过版权所有者的书面同意，
 * 任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 *
 */
package com.gws.configuration;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用tomcat配置
 *
 * @version 
 * @author wangdong  2016年4月21日 下午8:02:53
 * 
 */
@Configuration
public class TomcatConfig {
	@Value("${spring.server.port}")
	private String port;
	@Value("${spring.server.acceptorThreadCount}")
	private String acceptorThreadCount;
	@Value("${spring.server.minSpareThreads}")
	private String minSpareThreads;
	@Value("${spring.server.maxSpareThreads}")
	private String maxSpareThreads;
	@Value("${spring.server.maxThreads}")
	private String maxThreads;
	@Value("${spring.server.maxConnections}")
	private String maxConnections;
	@Value("${spring.server.protocol}")
	private String protocol;
	@Value("${spring.server.redirectPort}")
	private String redirectPort;
	@Value("${spring.server.compression}")
	private String compression;
	@Value("${spring.server.connectionTimeout}")
	private String connectionTimeout;

	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatFactory() {
		TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
		tomcatFactory.addConnectorCustomizers(new GwsTomcatConnectionCustomizer());
		return tomcatFactory;
	}

	public Connector httpsConnectBuilderFactory(){
		Connector connector = new Connector(Http11Nio2Protocol.class.getName());
		Http11Nio2Protocol httpProtocol = (Http11Nio2Protocol) connector.getProtocolHandler();
		connector.setMaxPostSize(200000);
		return connector;
	}

	/**
	 * 
	 * 默认http连接
	 *
	 * @version 
	 * @author wangdong  2016年7月20日 下午7:59:41
	 *
	 */
	public class GwsTomcatConnectionCustomizer implements TomcatConnectorCustomizer {

		public GwsTomcatConnectionCustomizer() {
		}

		@Override
		public void customize(Connector connector) {
			connector.setPort(Integer.valueOf(port));	
			connector.setAttribute("connectionTimeout", connectionTimeout);
	        connector.setAttribute("acceptorThreadCount", acceptorThreadCount);
	        connector.setAttribute("minSpareThreads", minSpareThreads);
	        connector.setAttribute("maxSpareThreads", maxSpareThreads);
	        connector.setAttribute("maxThreads", maxThreads);
	        connector.setAttribute("maxConnections", maxConnections);
	        connector.setAttribute("protocol", protocol);
	        connector.setAttribute("redirectPort", "redirectPort");
	        connector.setAttribute("compression", "compression");
		}
	}
}
