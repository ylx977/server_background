package com.gws.common.constants.backstage;

import com.sun.tools.javac.jvm.Code;
import lombok.Getter;

import javax.naming.Name;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/23.
 */
@Getter
public enum FrontUserApplyEnum {

    PHONE(1,"手机"),
    EMAIL(2,"邮箱"),
    ;

    FrontUserApplyEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }
    private Integer code;
    private String name;

}
