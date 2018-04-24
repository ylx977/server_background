package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
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
     * 多少人民币/1新加坡元
     */
    @Column(name = "cny_sgd")
    private Double cnysgd;

    /**
     * 买入点差
     */
    @Column(name = "buy_spread")
    private Double buySpread;

    /**
     * 买入点差
     */
    @Column(name = "sell_spread")
    private Double sellSpread;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
