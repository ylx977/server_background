package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/21.
 */
@Data
@Entity
@Table(name = "problem")
public class Problem {

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
    @Column(name = "is_deleted")
    private Integer deleted;
}
