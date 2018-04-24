package com.gws.common.constants.backstage;

/**
 * @author ylx
 * 后台redis缓存的常量配置类
 * Created by fuzamei on 2018/4/22.
 */
public class RedisConfig {
    private RedisConfig() {
        throw new AssertionError("instantiation is not permitted");
    }

    //======================================用户相关的redis配置================================================
    /**
     * 后台用户登录时根据用户名和密码查询的详细信息redis的key值前缀
     */
    public static final String LOGIN_USERDETAIL_PREFIX = "LOGIN_userDetailDTO";
    /**
     * 后台用户登录时详细信息redis的过期时间
     */
    public static final long LOGIN_USERDETAIL_TIMEOUT = 60L*2*1000;

    /**
     * 后台用户token的redis前缀
     */
    public static final String USER_TOKEN_PREFIX = "backUserToken";
    /**
     * 后台用户token的redis的过期时间
     */
    public static final long USER_TOKEN_TIMEOUT = 60L*2*1000;

    /**
     * 后台用户权限校验时用户详细信息的redis的key值前缀
     */
    public static final String USER_AUTH_PREFIX = "backUserAuthority";
    /**
     * 后台用户权限校验时用户详细信息的redis的key值前缀
     */
    public static final long USER_AUTH_TIMEOUT = -1L;


    //======================================平台相关的redis配置================================================

    /**
     * 后台所有权限信息的redis前缀
     */
    public static final String ALL_BACK_AUTHES_PREFIX = "ALL_backAuthes";

    /**
     * 后台轮播图基本配置信息(播放顺序和播放时间间隔)的redis前缀
     */
    public static final String BANNER_BASIC_CONFIG = "BANNER_BAISC_CONFIG";

}
