package com.gws.interceptor.backstage;

import com.gws.common.constants.backstage.RedisConfig;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 用于检查用户的操作是否超时
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Component
public class TimeoutInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeoutInterceptor.class);

    private final RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    public TimeoutInterceptor(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tokenAndUserId = request.getHeader("Authorization");
        Long uid = ValidationUtil.checkAndAssignLong(tokenAndUserId.split("&")[1]);
        String token = tokenAndUserId.split("&")[0].replace("Bearer","");
        LOGGER.info("用户:{},将token信息再次更新到redis中");
        try {
            //将token信息更新到redis
            LOGGER.info("用户:{},将token信息再次更新到redis中");
            redisTemplate.opsForValue().set(RedisConfig.USER_TOKEN_PREFIX+uid, token,RedisConfig.USER_TOKEN_TIMEOUT, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            //如果redis出现异常，正常让过
            LOGGER.error("用户:{},存入redis出现异常,直接通过该拦截器");
            return true;
        }

    }

}
