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
@Table(name = "user_changeinfo_apply")
public class FrontUserApplyInfo {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "change_type")
    private Integer changeType;

    @Column(name = "oldinfo")
    private String oldInfo;

    @Column(name = "newinfo")
    private String newInfo;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "apply_status")
    private Integer applyStatus;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
