package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by fuzamei on 2018/4/18.
 */
@Entity
@Data
@Table(name = "back_authes")
public class BackAuthes{

    @Id
    @Column(name = "auth_id")
    private Long authId;

    @Column(name = "auth_name")
    private String authName;

    @Column(name = "auth_url")
    private String authUrl;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
