
package com.gws.utils.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import com.gws.utils.CacheConstant;

/**
 * 全局ID生成器
 *
 * @version 
 * @author wangdong  2016年4月13日 下午11:26:55
 * 
 */
@Component
public  class IdGlobalGenerator {
	
    @Autowired
    private  RedisTemplate<Object, Object> redisClient;
    
    private static RedisAtomicLong counter;	
    

	/**
	 * 
	 * 获取ID
	 * 
	 * @author wangdong 2016年4月13日
	 * @return
	 */
	public  Long getSeqId() {
		Long seqId =null;
		try {
			seqId =getCacheSeqIncr();
		} catch (Exception e) {
		}
		return seqId;
	}
	
	/**
	 * 
	 * 细化粒度，以单表实体类名为序列前缀
	 * 
	 * @author wangdong 2016年4月21日
	 * @param clz
	 * @return
	 */
	public  Long getSeqId(Class<?> clz) {
		Long seqId =null;
		String prefix= clz.getSimpleName().toUpperCase();
		try {
			seqId =getCacheSeqIncr(prefix);
		} catch (Exception e) {
		}
		return seqId;
	}
	
	private static Long getRandNum() {
		Long incrNum = Math.round(Math.random() * 1000);
		return incrNum;
	}
	
	private static Long getLocalCacheSeq(){
		Long maxNum = System.currentTimeMillis();
		Long randNum = getRandNum();
		return maxNum * 1000 + randNum;
	}
	
	/**
	 * 
	 * 获取缓存ID系列值
	 * 
	 * @author wangdong 2016年4月15日
	 * @return
	 */
	private  Long getCacheSeqIncr(){
		 counter = new RedisAtomicLong(CacheConstant.CACHE_ID_GEN,
				 redisClient.getConnectionFactory(), getLocalCacheSeq());
		 Long seqId = counter.incrementAndGet();

		 if (1l == seqId) {
		 	seqId = getLocalCacheSeq();
            counter.set(seqId);
		 }
		 return seqId;
	}
	
	/**
	 * 
	 * 细化粒度获取自增ID
	 * 
	 * @author wangdong 2016年4月21日
	 * @param prefix
	 * @return
	 */
	private  Long getCacheSeqIncr(String prefix) {
		counter = new RedisAtomicLong(CacheConstant.CACHE_ID_GEN + prefix,
				redisClient.getConnectionFactory());
		Long seqId = counter.incrementAndGet();

		if (1l == seqId) {
			seqId = getLocalCacheSeq();
			counter.set(seqId);
		}
		return seqId;
	}

}
