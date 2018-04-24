package com.gws.dto.message;

import lombok.Data;

/**
 * @author : Kumamon 熊本同学
 * @Description:用户个人的消息记录
 * @Modified By:
 */
@Data
public class NoticesRecordDto {

    private Long id; //消息主键id

    private Long operationId; //运营消息的ID

    private Integer type; //消息类型

    private String title; //消息标题

    private String content; //消息内容

    private Integer osType; //操作系统类型,1,IOS,2,安卓

    private String url; //给以后的h5跳转用

    private String sender; //发送人

    private Integer isDelete; //是否删除：1、是，2、否

}
