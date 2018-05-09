package com.gws.entity.request.trade;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * @author WangBin
 */
@Data
public class RequestTradeVO {
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 交易数量
     */
    private BigDecimal amount;
    /**
     * 交易利率
     */
    private BigDecimal rate;
    /**
     * 兑换数量
     */
    private BigDecimal exchangeAmount;
    /**
     * 发送币的地址
     */
    private String sendAddress;
    /**
     * 交易币种
     */
    private Integer tradeCoinType;
    /**
     * 兑换币种
     */
    private Integer coinType;
    /**
     * 交易类型 1.市价  2.限价
     */
    private Integer tradeType;
    /**
     * 行为  1.买  2.卖
     */
    private Integer behavior;
}
