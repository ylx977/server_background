package com.gws.common.constants.backstage;

/**
 * @author Kang Faming
 * @description：
 * @date: 2018.04.20 18:26
 */
public enum ProblemType {
    // 登录/注册
    login(1),
    // 交易
    trade(2),
    // 提取黄金
    extract(3),
    // 充币
    pay(4),
    // 提币
    mention(5),
    // 认证问题
    identification(6),
    // 资产
    property(7),
    // 其他问题
    other(8),;
    private Integer code;
    private ProblemType(Integer code){
        this.code = code;
    }

    public static boolean isDefined(Integer code){
        for(ProblemType type:ProblemType.values()){
            if(type.code.equals(code)){
                return true;
            }
        }
        return false;
    }

}
