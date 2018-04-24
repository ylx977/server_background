
package com.gws.enums.date;

/**
 * 【月份对应的数量的枚举】
 * @version
 */
public enum MonthEnum {

	M1(1),
    M2(2),
	M3(3),
	M4(4),
	M5(5),
    ;

	private Integer code;

	private MonthEnum(Integer code){
		this.code = code;
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
}
