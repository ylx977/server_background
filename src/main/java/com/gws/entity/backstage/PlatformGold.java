package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/28.
 */
@Entity
@Data
@Table(name = "platform_gold")
public class PlatformGold {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "total_gold")
    private Double totalGold;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
