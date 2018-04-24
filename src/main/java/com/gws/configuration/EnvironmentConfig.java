
package com.gws.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件
 *
 * @version 
 * @author wangdong  2016年4月19日 下午9:22:32
 * 
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class EnvironmentConfig {
	@Value("${spring.domain}")
	private String webDomain;
	
	@Value("${db.showsql}")
	private Boolean showSql=false;
	
	@Value("${redis.host}")
	private String host;
	
	@Value("${redis.port}")
	private int port;

	public String getWebDomain() {
		return webDomain;
	}

	public void setWebDomain(String webDomain) {
		this.webDomain = webDomain;
	}

	public Boolean getShowSql() {
		return showSql;
	}

	public void setShowSql(Boolean showSql) {
		this.showSql = showSql;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	
}
