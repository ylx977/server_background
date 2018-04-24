package com.gws.utils.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

  

public abstract class AbstractBaseRedis {  
    @Autowired
    protected RedisTemplate redisTemplate;  
  
    public void setRedisTemplate(RedisTemplate redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }  
        
}  
