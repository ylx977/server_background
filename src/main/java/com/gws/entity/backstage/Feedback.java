package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Kang Faming
 * @description：反馈建议实体类
 * @date: 2018.04.18 17:17
 */
@Data
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "uid")
    private Long uid;
    @Column(name = "problem_type")
    private Integer problemType;
    @Column(name = "description")
    private String description;
    @Column(name = "enclosure")
    private String enclosure;
    @Column(name = "ctime")
    private Integer ctime;
    @Column(name = "utime")
    private Integer utime;
    @Column(name = "userName")
    private String userName;
    @Column(name = "email")
    private String email;
}
