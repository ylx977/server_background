package com.gws.utils.token;

/**
 * Created by fuzamei on 2018/6/4.
 */
public class TokenUtil {

    /**
     * 根据用户提供的token和uid组成一个统一的令牌号
     * @return
     */
    public static String packTokne(String token,long uid){
        return "Bearer"+token+"&"+uid;
    }

}
