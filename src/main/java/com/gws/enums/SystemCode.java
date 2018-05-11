
package com.gws.enums;

/**
 *【错误码枚举】
 *
 * @author
 */
public enum SystemCode implements CodeStatus {

    SUCCESS("200", "成功","success"),
    NEED_LOGIN("201", "未登录","you are not login yet"),
    BAD_REQUEST("202", "参数非法","illegal parameter"),
    NOT_IN_WHITELIST("203", "不在白名单内","you are not in the white list"),
    ILLEGAL_ACTION("204", "不合法的接口","illegal interface"),
    NOT_AUTH("205", "未授权","unauthorized"),
    SYS_ERROR("206", "系统内部出错","system inner error"),
    TOKEN_ERROR("207", "token验证失败","fail to verify token"),
    VALIDATION_ERROR("208", "校验失败","fail to validate your parameter"),
    FAIL_LOGIN("209", "登录失败","fail to login"),
    WRONG_USER_PWD("210", "用户名密码错误","wrong password or username"),

    //==================以下是成功部分====================
    SUCCESS_LOGIN("200","登录成功","login success")

    ;



    private String code;

    private String message_cn;

    private String message_en;

    private SystemCode(String code, String message_cn, String message_en) {
        this.code = code;
        this.message_cn = message_cn;
        this.message_en = message_en;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String  code) {
        this.code = code;
    }

    @Override
    public String getMessageCN() {
        return message_cn;
    }

    @Override
    public String getMessageEN() {
        return message_en;
    }
}
