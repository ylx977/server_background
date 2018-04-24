package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by fuzamei on 2018/4/18.
 */
@Entity
@Data
@Table(name = "back_authgroups")
public class BackAuthgroups {

    @Id
    @Column(name = "authgroup_id")
    private Long authgroupId;

    @Column(name = "authgroup_name")
    private String authgroupName;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;
}
