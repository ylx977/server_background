package com.gws.configuration.backstage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/30.
 */
@Component
@Scope(value = "singleton")
public class UidConfig {

    public static final ThreadLocal<Long> uidMark = new ThreadLocal<>();

    public static void setUid(long uid){
        uidMark.set(uid);
    }

    public static long getUid(){
        return uidMark.get();
    }

    public static void remove(){
        uidMark.remove();
    }

}
