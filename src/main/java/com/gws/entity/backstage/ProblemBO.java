package com.gws.entity.backstage;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/21.
 */
@Data
public class ProblemBO {

    private Integer page;

    private Integer rowNum;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告编号
     */
    private Long id;

    /**
     * 公告作者
     */
    private String author;

    /**
     * 内容id，和公告编号id是一一对应的
     */
    private Long contentId;

    /**
     * 公告内容
     */
    private String problemContent;
}
