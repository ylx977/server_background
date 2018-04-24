package com.gws.utils.webservice;

import com.gws.enums.SystemCode;

/**
 * api接口请求统一响应结果
 * @author wangdong
 */
public class RetResult {

	private String code;

	private String message;

	private Object data;

	public RetResult(String code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public RetResult(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public RetResult(SystemCode codeStatus, Object data) {
		this.code = codeStatus.getCode();
		this.message = codeStatus.getMessage();
		this.data = data;
	}
	
	public RetResult(SystemCode codeStatus) {
		this.code = codeStatus.getCode();
		this.message = codeStatus.getMessage();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


}
