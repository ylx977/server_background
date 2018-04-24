package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/23.
 */
@Entity
@Data
@Table(name = "back_golden_code")
public class BackGoldenCode {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "golden_code")
    private String goldenCode;

    @Column(name = "golden_withdraw_id")
    private Long goldenWithdrawIid;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
