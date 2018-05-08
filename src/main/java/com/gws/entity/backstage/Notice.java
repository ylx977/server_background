package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Kang Faming
 * @description：
 * @date: 2018.04.19 14:22
 */
@Data
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "uid")
    private Long uid;
    @Column(name = "ctime")
    private Integer ctime;
    @Column(name = "utime")
    private Integer utime;
    @Column(name = "is_show")
    private Integer show;
    @Column(name = "is_top")
    private Integer top;
    @Column(name = "is_deleted")
    private Integer deleted;
}
