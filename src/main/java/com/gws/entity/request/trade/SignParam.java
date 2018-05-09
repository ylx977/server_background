package com.gws.entity.request.trade;

import lombok.Data;
import lombok.ToString;

/**
 * @author WangBin
 */
@Data
@ToString
public class SignParam {

    private String operatorPriKey;
    private String fromAddress;
    private String toAddress;
    private Long amount;
    private int symbolId;
    private long hash;

    public SignParam(){
    }

    public SignParam(String operatorPriKey, String fromAddress, String toAddress, Long amount, int symbolId, long hash){
        this.operatorPriKey = operatorPriKey;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.symbolId = symbolId;
        this.hash = hash;
    }
}
