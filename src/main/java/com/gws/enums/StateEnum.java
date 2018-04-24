
package com.gws.enums;

/**
 * 【游戏账号基本类型属性状态枚举】
 *
 * @version 
 * @author wenfei  2017年4月6日 下午6:33:49
 * 
 */
public enum StateEnum {

	NORMAL(1, "正常"),
    DELETED(-1, "删除"),
    ;
	
	private Integer code;
	private String message;
	
	private StateEnum(Integer code, String message){
		this.code = code;
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
