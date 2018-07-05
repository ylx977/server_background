package com.gws.configuration.backstage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/30.
 */
@Component
@Scope(value = "singleton")
public class LangConfig {

    public static final ThreadLocal<Integer> langMark = new ThreadLocal<>();

    public static void setLang(int lang){
        langMark.set(lang);
    }

    public static int getLang(){
        return langMark.get();
    }

    public static void remove(){
        langMark.remove();
    }

}
