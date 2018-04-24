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
 * 【年份管理】实体类
*
 * @version
 */
@Data
@Entity
@Table(name = "date_test")
public class DateTest implements Serializable {

	@Id
	@Column(name = "id")
	private Integer id;

	//年份
	@Column(name = "year")
	private Integer year;

	//月份
	@Column(name = "month")
	private Integer month;

	//数字
	@Column(name = "amount")
	private BigDecimal amount;

}

