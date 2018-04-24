package com.gws.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.util.StringUtils;

import com.gws.utils.GwsUtil;

/**
 * 
 * 【redisKey值初始化】
 *
 * @version 
 * @author wangdong  2016年5月9日 下午5:57:13
 *
 */
@Configuration
public class RedisValInit implements CommandLineRunner {
		
    @Autowired
    private  RedisTemplate<Object, Object> redisClient;
    
	@Autowired @Qualifier("initSeqId")
	private RedisValConfig redisConfig;
	
	@Override
	public void run(String... arg0) throws Exception {
		if(null!=redisConfig){
			String prefix = redisConfig.getPrefix();
			String key = redisConfig.getKey();
			if(!StringUtils.isEmpty(key)){
				String[] keys = key.split(",");
				if(keys.length>0){
					for(String k:keys){
						new RedisAtomicLong(prefix + k.trim().toUpperCase(),redisClient.getConnectionFactory(),GwsUtil.getSeq());
					}
				}
			}
		}
	}
	
}
