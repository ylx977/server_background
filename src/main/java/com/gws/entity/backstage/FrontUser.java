package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/20.
 */
@Entity
@Data
@Table(name = "user_base_info")
public class FrontUser {

    @Id
    @Column(name = "uid")
    private Long uid;

    @Column(name = "user_name")
    private String username;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "user_status")
    private Integer userStatus;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
