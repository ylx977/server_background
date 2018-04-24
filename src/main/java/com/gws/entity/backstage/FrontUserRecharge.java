package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Entity
@Data
@Table(name = "user_rechargecoin")
public class FrontUserRecharge {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "coin_type")
    private Integer coinType;

    @Column(name = "inner_address")
    private String innerAddress;

    @Column(name = "outer_address")
    private String outerAddress;

    @Column(name = "coin_amount")
    private Double coinAmount;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
