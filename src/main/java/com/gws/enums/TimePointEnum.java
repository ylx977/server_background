
package com.gws.enums;

/**
 * 【操作系统枚举】
 *
 * @author wangdong
 *
 */
public enum TimePointEnum {

	SECOND(1,"秒前"),
	MINUTE(2, "分钟前"),
	HOUR(4, "小时前"),
	YESTERDAY(5,"昨天"),
	DAY(6,"天前")
    ;

	private Integer code;
	private String message;

	private TimePointEnum(Integer code, String message){
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


	public static TimePointEnum getEnum(Integer vCodeType) {
		for (TimePointEnum timePointEnum: TimePointEnum.values()) {
			if (timePointEnum.getCode().equals(vCodeType)) {
				return timePointEnum;
			}
		}
		return null;
	}
	
	
}
