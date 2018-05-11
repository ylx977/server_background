package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/10.
 */
@Entity
@Data
@Table(name = "platform_bty_addresses")
public class BtyAddresses {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "is_default")
    private Integer isDefault;

    @Column(name = "tag")
    private String tag;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
