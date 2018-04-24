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
@Table(name = "back_users_token")
public class BackUserToken {

    @Id
    @Column(name = "uid")
    private Long uid;

    @Column(name = "token")
    private String token;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
