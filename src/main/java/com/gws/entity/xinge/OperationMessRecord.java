
package com.gws.entity.xinge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 【运营操作消息记录表】实体类
*
 * @version
 * @author wangdong 2017年12月06日 上午09:25:20
 */ 
@Entity
@Table(name = "t_operation_mess_record") 
public class OperationMessRecord {

	@Id
	@Column(name = "id")
	private Long id; //操作后台的消息id

	@Column(name = "type")
	private Integer type; //消息类型,1,个人消息,2,系统消息

	@Column(name = "title")
	private String title; //消息标题

	@Column(name = "content")
	private String content; //消息内容

	@Column(name = "sender")
	private String sender; //发送人

	@Column(name = "receiver")
	private String receiver; //接收人

	@Column(name = "url")
	private String url; //给以后的h5跳转用

	@Column(name = "is_delete")
	private Integer isDelete; //是否删除：1、是，2、否

	@Column(name = "ctime")
	private Integer ctime; //创建时间

	@Column(name = "utime")
	private Integer utime; //更新时间


	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type=type;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content=content;
	}

	public String getSender(){
		return sender;
	}

	public void setSender(String sender){
		this.sender=sender;
	}

	public String getReceiver(){
		return receiver;
	}

	public void setReceiver(String receiver){
		this.receiver=receiver;
	}

	public Integer getIsDelete(){
		return isDelete;
	}

	public void setIsDelete(Integer isDelete){
		this.isDelete=isDelete;
	}

	public Integer getCtime(){
		return ctime;
	}

	public void setCtime(Integer ctime){
		this.ctime=ctime;
	}

	public Integer getUtime(){
		return utime;
	}

	public void setUtime(Integer utime){
		this.utime=utime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

