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
@Table(name = "problem_content")
public class ProblemContent {

    @Id
    @Column(name = "content_id")
    private Long id;
    @Column(name = "content")
    private String content;

}
