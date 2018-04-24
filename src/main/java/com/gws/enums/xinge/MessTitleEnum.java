
package com.gws.enums.xinge;

/**
 * 【消息类型的枚举】
 *
 * @version 
 * @author wenfei  2017年4月6日 下午6:33:49
 * 
 */
public enum MessTitleEnum {

	NEWUSERLOGIN(1, "欢迎加入游戏猫"),
    USERLOGIN(2, "欢迎您回到游戏猫"),
	ACOUNTCHANGE(3, "账户变动"),
	SYSTEM(4, "版本升级啦"),
    ;

	private Integer code;
	private String message;

	private MessTitleEnum(Integer code, String message){
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

	public static MessTitleEnum getEnum(Integer code) {
		for (MessTitleEnum messTitleEnum: MessTitleEnum.values()) {
			if (messTitleEnum.getCode().equals(code)) {
				return messTitleEnum;
			}
		}
		return null;
	}
}
