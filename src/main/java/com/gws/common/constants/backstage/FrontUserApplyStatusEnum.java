package com.gws.common.constants.backstage;

import lombok.Getter;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/23.
 */
@Getter
public enum FrontUserApplyStatusEnum {

    TO_CHECK(1,"待审核"),
    APPROVE(2,"通过"),
    REJECT(3,"拒绝");

    FrontUserApplyStatusEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }
    private Integer code;
    private String name;

}
