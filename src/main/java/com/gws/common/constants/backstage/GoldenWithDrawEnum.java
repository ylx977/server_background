package com.gws.common.constants.backstage;

import lombok.Getter;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Getter
public enum GoldenWithDrawEnum {

    APPLY(0,"申请中"),
    TO_WITHDRAW(1,"待提取"),
    CHECKOUT(2,"已出货");

    GoldenWithDrawEnum(Integer status,String statusName){
        this.status = status;
        this.statusName = statusName;
    }
    private Integer status;
    private String statusName;


}
