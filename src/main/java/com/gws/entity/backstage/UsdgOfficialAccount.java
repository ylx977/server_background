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
@Table(name = "usdg_official_account")
public class UsdgOfficialAccount {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private Integer type;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "seed")
    private String seed;

    @Column(name = "publickey")
    private String publicKey;

    @Column(name = "privatekey")
    private String privateKey;

    @Column(name = "real_amount")
    private Double realAmount;

    @Column(name = "usable_amount")
    private Double usableAmount;

    @Column(name = "freeze_amount")
    private Double freezeAmount;

    @Column(name = "height")
    private Long height;

    @Column(name = "indexes")
    private Long indexes;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

    @Column(name = "prikey")
    private String prikey;

    @Column(name = "pubkey")
    private String pubkey;
}
