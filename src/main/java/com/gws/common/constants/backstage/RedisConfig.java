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
    public static final long LOGIN_USERDETAIL_TIMEOUT = 60L*5*1000;

    /**
     * 后台用户token的redis前缀
     */
    public static final String USER_TOKEN_PREFIX = "backUserToken";
    /**
     * 后台用户token的redis的过期时间
     */
    public static final long USER_TOKEN_TIMEOUT = 60L*30*1000;

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

    /**
     * 用于存放市价信息的
     */
    public static final String MARKET_PRICE = "MARKET_PRICE";

    /**
     * 公告内容
     */
    public static final String NOTICE_CONTENT = "NOTICE_CONTENT";

    /**
     * 常见问题内容
     */
    public static final String PROBLEM_CONTENT = "PROBLEM_CONTENT";

    /**
     * 比特元和USDG之间的交易费
     */
    public static final String BTY_COIN_FEE = "BTY_COIN_FEE";

    /**
     * 不同币的提币费
     */
    public static final String COIN_DRAW_FEE = "COIN_DRAW_FEE";

    /**
     * 不同黄金提取的提币费
     */
    public static final String GOLD_DRAW_FEE = "GOLD_DRAW_FEE";

}
