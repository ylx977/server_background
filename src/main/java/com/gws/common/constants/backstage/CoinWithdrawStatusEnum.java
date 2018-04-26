package com.gws.common.constants.backstage;

import lombok.Getter;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/26.
 */
@Getter
public enum CoinWithdrawStatusEnum {

    TO_CHECK(1,"待审核"),
    FIRST_PASS(2,"初审通过"),
    SECOND_PASS(3,"复审通过"),
    REJECT(4,"拒绝");

    CoinWithdrawStatusEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }
    private Integer code;
    private String name;

}
