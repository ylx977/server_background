package com.gws.dto.message;

import lombok.Data;

/**
 * @author : Kumamon 熊本同学
 * @Description:用户个人的消息记录
 * @Modified By:
 */
@Data
public class UserMessage {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 账号Id,账户变动的各种ID
     */
    private Long accountId;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 给以后的h5跳转用
     */
    private String url;

    /**
     * 发送人
     */
    private String sender;

    /**
     * 操作系统类型
     */
    private Integer osType;

    /**
     * 是否删除：1、是，2、否
     */
    private Integer isDelete;

    /**
     * 时间点
     */
    private String timePoint;

    /**
     * 创建时间
     */
    private Integer ctime;

}
