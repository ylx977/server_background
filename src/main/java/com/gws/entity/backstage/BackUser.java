package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Entity
@Data
@Table(name = "back_users")
public class BackUser {
    @Id
    @Column(name = "uid")
    private Long uid;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "contact")
    private String contact;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

    @Column(name = "is_deleted")
    private Integer isDelete;
}
