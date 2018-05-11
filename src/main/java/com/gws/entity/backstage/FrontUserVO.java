package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/10.
 */
@Data
public class FrontUserVO {

    private Long uid;

    private String username;

    private String phoneNumber;

    private String emailAddress;

    private Integer userStatus;

    private Integer gender;

    private Integer ctime;

    private Integer utime;

    private Integer cardType;

    private String cardNumber;

    private String realName;
}
