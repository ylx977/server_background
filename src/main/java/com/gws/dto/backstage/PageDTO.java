package com.gws.dto.backstage;


import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This is a pagination DTO
 * 
 * @author ylx
 * @modifier ylx[yanglingxiao2009@163.com]
 */
@Data
public class PageDTO {
	
	private PageDTO() {}
	
	public static final <T> PageDTO getPagination(Long total, List<T> rows){
		PageDTO pagination = new PageDTO();
		pagination.setRows(rows);
		pagination.setTotal(total);
		return pagination;
	}

	/**
	 * 需要显示的数据
	 */
	private List rows;

	/**
	 * 总的页数
	 */
	private Long total;


}