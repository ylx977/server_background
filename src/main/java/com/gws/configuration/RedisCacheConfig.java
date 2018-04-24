
package com.gws.configuration;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.gws.base.CacheModule;
import com.gws.base.CachePrefix;
import com.gws.utils.cache.BaseRedisCacheImpl;
import com.gws.utils.cache.CacheClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.Map;


/**
 * 
 * redis配置
 *
 * @version 
 * @author wangdong  2016年4月15日 下午3:07:39
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableCaching
public class RedisCacheConfig {
	
	@Value("${redis.host}")
	private String host;
	
	@Value("${redis.port}")
	private int port;
	
	@Value("${redis.password}")
	private String password;

    @Value("${redis.pool.maxTotal}")
    private int maxTotal;
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<Object, Object> redisTemplate;
	
	// redis缓存失效时间, 24小时
	private Integer cacheExpirationTime = 60 * 60;

    @Bean
    public JedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        if(StringUtils.isNotEmpty(password)){
        	factory.setPassword(password);
        }
        factory.setPoolConfig(jedisPoolConfig);
        return factory;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setBlockWhenExhausted(true);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setMaxTotal(maxTotal);
        return jedisPoolConfig;
    }

	@Bean
	public CacheClient redisClientFactory() {
		CacheClient cc = new BaseRedisCacheImpl();
		return cc;
	}

    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setKeySerializer(new StringRedisSerializer()); // 对于普通K-V操作时，key采取的序列化策略
		template.setValueSerializer(serializer); // value采取的序列化策略
		template.setHashKeySerializer(serializer); // 在hash数据结构中，hash-key的序列化策略
		template.setHashValueSerializer(serializer); // 在hash数据结构中，hash-key的序列化策略
		template.setConnectionFactory(factory);
		template.afterPropertiesSet();
		return template;
    }
    
	@Bean
	public CacheManagerCustomizer<RedisCacheManager> cacheManagerCustomizer() {
		return new CacheManagerCustomizer<RedisCacheManager>() {
			@Override
			public void customize(RedisCacheManager cacheManager) {
				cacheManager.setDefaultExpiration(cacheExpirationTime);
				Map<String, Long> expires = Maps.newHashMap();
				expires.put(CacheModule.GAME, 2*3600l);
				expires.put(CacheModule.APP, 24*3600l);
				cacheManager.setExpires(expires);

				cacheManager.setCacheNames(
						Arrays.asList(CacheModule.COMMENT,
								CacheModule.GAME,
								CacheModule.USER,
								CacheModule.WEB,
								CacheModule.APP,
								CacheModule.SHARE));
			}

		};
	}
}
	