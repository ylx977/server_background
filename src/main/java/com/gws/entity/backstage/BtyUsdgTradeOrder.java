package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/26.
 */
@Entity
@Data
@Table(name = "bty_usdg_trade_order")
public class BtyUsdgTradeOrder {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "bty_amount")
    private Double btyAmount;

    @Column(name = "usdg_amount")
    private Double usdgAmount;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "user_bty_address")
    private String userBtyAddress;

    @Column(name = "user_usdg_address")
    private String userUsdgAddress;

    @Column(name = "platform_bty_address")
    private String platformBbtyAddress;

    @Column(name = "platform_usdg_address")
    private String platformUsdgAddress;

    @Column(name = "trade_type")
    private Integer tradeType;

    @Column(name = "trade_status")
    private Integer tradeStatus;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
