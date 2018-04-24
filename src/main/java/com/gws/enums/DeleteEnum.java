
package com.gws.enums;

/**
 * 【删除枚举】
 *
 * @version 
 * @author wenfei  2017年4月6日 下午6:33:49
 * 
 */
public enum DeleteEnum {

	DELETE(1, "是,删除"),
    NOTDELETE(2, "否,不删除")
    ;

	private Integer code;
	private String message;

	private DeleteEnum(Integer code, String message){
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


	public static DeleteEnum getEnum(Integer vCodeType) {
		for (DeleteEnum deleteEnum: DeleteEnum.values()) {
			if (deleteEnum.getCode().equals(vCodeType)) {
				return deleteEnum;
			}
		}
		return null;
	}
	
	
}
