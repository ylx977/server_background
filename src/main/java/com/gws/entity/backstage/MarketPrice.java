package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/21.
 */
@Entity
@Data
@Table(name = "market_price")
public class MarketPrice {

    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 多少人民币/1新加坡元--->买入价
     */
    @Column(name = "buy_cny_sgd")
    private Double buyCnySgd;

    /**
     * 多少人民币/1新加坡元--->卖出价
     */
    @Column(name = "sell_cny_sgd")
    private Double sellCnySgd;

    /**
     * 多少新元/1美元--->买入价
     */
    private Double buySgdUsd;

    /**
     * 多少新元/1美元--->卖出价
     */
    private Double sellSgdUsd;

    /**
     * 买入点差
     */
    @Column(name = "buy_spread")
    private Double buySpread;
    /**
     * 卖出点差
     */
    @Column(name = "sell_spread")
    private Double sellSpread;

    /**
     * 交易手续费，1个BTY中，收取的BTY数量
     */
    @Column(name = "trade_fee")
    private Double tradeFee;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
