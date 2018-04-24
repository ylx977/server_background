package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/24.
 */
@Entity
@Data
@Table(name = "user_identity")
public class UserIdentity {

    @Id
    @Column(name = "uid")
    private Long uid;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "nationality")
    private Integer nationality;

    @Column(name = "card_type")
    private Integer cardType;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "front_photo")
    private String frontPhoto;

    @Column(name = "back_photo")
    private String backPhoto;

    @Column(name = "hand_photo")
    private String handPhoto;

    @Column(name = "user_video")
    private String userVideo;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
