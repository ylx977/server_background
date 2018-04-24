
package com.gws.enums;

/**
 * 是与否枚举类
 * @author wangdong
 */
public enum YesNoEnum {
	YES(1,"是"),
	NO(2,"否")
	;
	private Integer code;
	private String message;

	private YesNoEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}


	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static YesNoEnum getEnum(Integer code){
		if (code == null)
            return null;
        for (YesNoEnum yesNoEnum: YesNoEnum.values()) {
            if (yesNoEnum.getCode().equals(code))
                return yesNoEnum;
        }
        return null;
	}
}
