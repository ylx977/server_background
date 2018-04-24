/**
 *Copyright (C) 2016  HangZhou YuShi Technology Co.Ltd  Holdings Ltd. All rights reserved
*
*本代码版权归杭州宇石科技所有，且受到相关的法律保护。
*没有经过版权所有者的书面同意，任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 * 
 */ 
package com.gws.entity.xinge;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 【广播通知记录表】实体类
*
 * @version
 * @author wangdong 2017年11月24日 上午10:23:14
 */ 
@Entity
@Data
@Table(name = "t_notices_record") 
public class NoticesRecord {

	@Id
	@Column(name = "id")
	private Long id; //消息主键id

	@Column(name = "account_id")
	private Long accountId; //活动Id,各种活动ID

	@Column(name = "operation_id")
	private Long operationId; //运营消息的ID

	@Column(name = "type")
	private Integer type; //消息类型

	@Column(name = "title")
	private String title; //消息标题

	@Column(name = "content")
	private String content; //消息内容

	@Column(name = "os_type")
	private Integer osType; //操作系统类型,1,IOS,2,安卓

	@Column(name = "url")
	private String url; //给以后的h5跳转用

	@Column(name = "sender")
	private String sender; //发送人

	@Column(name = "is_delete")
	private Integer isDelete; //是否删除：1、是，2、否

	@Column(name = "ctime")
	private Integer ctime; //创建时间

	@Column(name = "utime")
	private Integer utime; //更新时间

}

