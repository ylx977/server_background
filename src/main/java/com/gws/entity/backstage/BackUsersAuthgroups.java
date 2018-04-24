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
@Table(name = "back_users_authgroups")
public class BackUsersAuthgroups {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "authgroup_id")
    private Long authgroupId;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;
}
