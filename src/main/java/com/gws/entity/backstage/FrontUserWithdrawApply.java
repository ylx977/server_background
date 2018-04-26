package com.gws.entity.backstage;

import com.gws.common.constants.backstage.CoinType;
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
@Table(name = "user_drawcoin_apply")
public class FrontUserWithdrawApply {

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

    @Column(name = "coin_type")
    private Integer coinType;

    @Column(name = "inner_address")
    private String innerAddress;

    @Column(name = "outer_address")
    private String outerAddress;

    @Column(name = "coin_amount")
    private Double coinAmount;

    @Column(name = "miner_amount")
    private Double minerAmount;

    @Column(name = "apply_status")
    private Integer applyStatus;

    @Column(name = "first_check_uid")
    private Long firstCheckUid;

    @Column(name = "first_check_name")
    private String firstCheckName;

    @Column(name = "second_check_uid")
    private Long secondCheckUid;

    @Column(name = "second_check_name")
    private String secondCheckName;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;


}
