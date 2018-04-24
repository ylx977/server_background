package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Entity
@Data
@Table(name = "back_authgroups_authes")
public class BackAuthgroupsAuthes {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "authgroup_id")
    private Long authgroupId;

    @Column(name = "auth_id")
    private Long authId;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;
}
