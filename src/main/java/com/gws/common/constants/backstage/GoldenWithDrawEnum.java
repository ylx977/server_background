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
    CHECKOUT(2,"已出货"),
    TO_AUDIT(3,"后台审核中"),
    WITHDRAW_FAIL(4,"提取失败");

    GoldenWithDrawEnum(Integer status,String statusName){
        this.status = status;
        this.statusName = statusName;
    }
    private Integer status;
    private String statusName;


}
