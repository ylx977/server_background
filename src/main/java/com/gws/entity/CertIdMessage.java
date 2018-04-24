/**
 *Copyright (C) 2016  HangZhou YuShi Technology Co.Ltd  Holdings Ltd. All rights reserved
*
*本代码版权归杭州宇石科技所有，且受到相关的法律保护。
*没有经过版权所有者的书面同意，任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 * 
 */ 
package com.gws.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 证书和消息管理的实体类
 * @author wangdong
 * @version
 */
@Data
@Entity
@Table(name = "t_cert_message")
public class CertIdMessage implements Serializable {

	@Id
	@Column(name = "id")
	private Long id;

	//证书Id
	@Column(name = "cert_id")
	private Long certId;

	//信鸽的accessId
	@Column(name = "access_id")
	private Long accessId;

	//信鸽的secretKey
	@Column(name = "secret_key")
	private Long secretKey;

	//是否删除，0，是，1，否
	@Column(name = "is_delete")
	private Integer isDelete;

	//创建时间
	@Column(name = "ctime")
	private Integer ctime;

	//更新时间
	@Column(name = "utime")
	private Integer utime;
}

