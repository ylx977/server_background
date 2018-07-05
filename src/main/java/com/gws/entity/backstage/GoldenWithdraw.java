package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Entity
@Data
@Table(name = "back_golden_withdraw")
public class GoldenWithdraw {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "withdraw_unit")
    private Integer withdrawUnit;

    @Column(name = "withdraw_amount")
    private Integer withdrawAmount;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "withdraw_time")
    private Integer withdrawTime;

    @Column(name = "fee")
    private Double fee;

    @Column(name = "total")
    private Double total;

    @Column(name = "hash")
    private String hash;
}
