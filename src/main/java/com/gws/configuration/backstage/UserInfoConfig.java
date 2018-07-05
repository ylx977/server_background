package com.gws.configuration.backstage;

import com.gws.dto.backstage.UserDetailDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/11.
 */
@Component
@Scope(value = "singleton")
public class UserInfoConfig {

    public static final ThreadLocal<UserDetailDTO> userInfo = new ThreadLocal<>();

    public static void setUserInfo(UserDetailDTO userDetailDTO){
        userInfo.set(userDetailDTO);
    }

    public static UserDetailDTO getUserInfo(){
        return userInfo.get();
    }

    public static void remove(){
        userInfo.remove();
    }

}
