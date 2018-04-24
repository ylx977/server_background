
package com.gws.enums.xinge;

/**
 * 【消息类型的枚举】
 *
 * @version 
 * @author wenfei  2017年4月6日 下午6:33:49
 * 
 */
public enum MessContentEnum {

	NEWUSERLOGIN(1, "欢迎加入游戏猫，高端玩家俱乐部。每日精品游戏推荐，专业游戏点评，游戏评分。快点来大显身手，成为我们的点评大咖。"),
    USERLOGIN(2, "欢迎您再次回来，如果您在使用中有任何疑问或者需要帮助，请关注微信公众号”游戏猫“，选择在线客服与我们取得联系，喵！"),
	MGAECONSUME(3, "游戏消费"),
	MIAORECHARGE(4, "喵点充值"),
	APPUPGRADE(5, "游戏猫APP升级啦！更新最新版游戏猫APP，在XXXX可获得最新的福利哦。游戏猫APP升级啦！更新最新版游戏猫APP，在XXXX可获得最新的福利哦。游戏猫APP升级啦！更新最新版游戏猫APP，在XXXX可获得福利"),
    ;

	private Integer code;
	private String message;

	private MessContentEnum(Integer code, String message){
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

	public static MessContentEnum getEnum(Integer code) {
		for (MessContentEnum messContentEnum: MessContentEnum.values()) {
			if (messContentEnum.getCode().equals(code)) {
				return messContentEnum;
			}
		}
		return null;
	}
}
