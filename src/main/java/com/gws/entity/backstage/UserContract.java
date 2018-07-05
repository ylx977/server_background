package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/29.
 */
@Entity
@Data
@Table(name = "user_contract")
public class UserContract {

    @Id
    @Column(name = "uid")
    private Long uid;

    @Column(name = "privatekey")
    private String privatekey;

    @Column(name = "publickey")
    private String publickey;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
