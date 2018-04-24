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
@Table(name = "back_banner_config")
public class BannerConfig {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "display_interval")
    private Integer displayInterval;

    @Column(name = "ctime")
    private Integer ctime;

    @Column(name = "utime")
    private Integer utime;

}
