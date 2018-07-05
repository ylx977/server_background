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
@Table(name = "market_price_history")
public class MarketPriceHistory {

    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 多少BTY/1USDG--->买入价
     */
    @Column(name = "buy_bty_usdg")
    private Double buyBtyUsdg;

    /**
     * 多少BTY/1USDG--->卖出价
     */
    @Column(name = "sell_bty_usdg")
    private Double sellBtyUsdg;

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
     * 交易手续费，一个BTY中收取BTY的数量
     */
    @Column(name = "trade_fee")
    private Double tradeFee;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
