package com.gws.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * 【redisKey值初始化】
 *
 * @version 
 * @author wangdong  2016年5月9日 下午5:57:13
 *
 */
@Configuration
public class RedisValConfig{
	
	private String prefix;
	
	private String key;
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Bean(name="initSeqId")
	public RedisValConfig initSeqId() {
		return new RedisValConfig();
	}
	
}
