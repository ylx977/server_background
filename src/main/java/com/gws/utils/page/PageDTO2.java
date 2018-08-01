package com.gws.utils.page;


import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This is a pagination DTO
 * @author ylx
 * @modifier ylx[yanglingxiao2009@163.com]
 */
@Setter
@Getter
@ToString
public class PageDTO2 {
	
	private PageDTO2() {}
	
	public static final <T> PageDTO2 getPagination(int total, List<T> rows){
		PageDTO2 pagination = new PageDTO2();
		pagination.setRows(rows);
		pagination.setTotal(total);
		return pagination;
	};
	
	@SuppressWarnings("rawtypes")
	private List rows;//需要显示的数据

	private int total;//总的页数


}