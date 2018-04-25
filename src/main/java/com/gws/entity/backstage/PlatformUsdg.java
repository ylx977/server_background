package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/25.
 */
@Entity
@Data
@Table(name = "platform_usdg")
public class PlatformUsdg {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "total_usdg")
    private Double totalUsdg;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
