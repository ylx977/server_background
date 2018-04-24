
package com.gws.enums.xinge;

/**
 * 【消息类型的枚举】
 *
 * @version 
 * @author wenfei  2017年4月6日 下午6:33:49
 * 
 */
public enum MessTypeEnum {

	ACCOUNTCHANGES(1, "账户变动"),
	UPGRADE(2, "版本升级"),
	GAMERESERVATION(3,"新游预约"),
	SYSTEMMESSAGES(4,"系统消息"),
	OPERATIONMESS(5,"运营广播消息"),
	OPERATIONNOTICE(6,"运营个人消息"),
	;

	private Integer code;
	private String message;

	private MessTypeEnum(Integer code, String message){
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

	public static MessTypeEnum getEnum(Integer code) {
		for (MessTypeEnum messTypeEnum: MessTypeEnum.values()) {
			if (messTypeEnum.getCode().equals(code)) {
				return messTypeEnum;
			}
		}
		return null;
	}
}
