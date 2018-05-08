package com.gws.entity.backstage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Kang Faming
 * @description：
 * @date: 2018.04.19 14:40
 */
@Data
@Entity
@Table(name = "notice_content")
public class NoticeContent {
    @Id
    @Column(name = "content_id")
    private Long id;
    @Column(name = "content")
    private String content;
}
