package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/27.
 */
@Entity
@Data
@Table(name = "coin_fee")
public class CoinFee {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "buy_trade")
    private Double buyTrade;
    @Column(name = "sell_trade")
    private Double sellTrade;
    @Column(name = "draw")
    private Double draw;
    @Column(name = "ctime")
    private Integer ctime;
    @Column(name = "utime")
    private Integer utime;

}
