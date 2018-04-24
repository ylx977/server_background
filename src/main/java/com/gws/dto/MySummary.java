package com.gws.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySummary<T, M> {


    private List<T> list = new ArrayList<T>();

    /**
     * 总页数
     */
    private long total;

    private M summary;


	public MySummary() {
	    this.list = Collections.EMPTY_LIST;
	    this.total = 0;
	}

	public MySummary(List<T> list, long total) {
		this.list = list;
		this.total = total;
	}

	public MySummary(List<T> list, long total, M summary) {
		this.list = list;
		this.total = total;
		this.summary = summary;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public M getSummary() {
		return summary;
	}

	public void setSummary(M summary) {
		this.summary = summary;
	}
}
