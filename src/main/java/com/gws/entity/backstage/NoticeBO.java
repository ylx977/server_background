package com.gws.entity.backstage;

import com.gws.dto.backstage.UserDetailDTO;
import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
@Data
public class NoticeBO {

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
    private String noticeContent;

    private UserDetailDTO userDetailDTO;

    /**
     * 1表示修改是否置顶 2表示修改是否显示
     */
    private Integer type;

    /**
     * 如果是置顶1表示置顶0表示不置顶
     * 如果是显示1表示显示0表示不显示
     */
    private Integer status;

    private Integer lang;

}
