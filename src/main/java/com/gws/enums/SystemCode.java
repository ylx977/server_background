
package com.gws.enums;

/**
 *【错误码枚举】
 *
 * @author
 */
public enum SystemCode implements CodeStatus {

    SUCCESS("200", "成功"),
    NEED_LOGIN("201", "未登录"),
    BAD_REQUEST("202", "参数非法"),
    NOT_IN_WHITELIST("203", "不在白名单内"),
    ILLEGAL_ACTION("204", "不合法的接口"),
    NOT_AUTH("205", "未授权"),
    SYS_ERROR("206", "系统内部出错"),
    TOKEN_ERROR("207", "token验证失败"),
    VALIDATION_ERROR("208", "校验失败"),
    FAIL_LOGIN("209", "登录失败"),
    WRONG_USER_PWD("210", "用户名密码错误"),

    //==================以下是成功部分====================
    SUCCESS_LOGIN("200","登录成功")

    ;



    private String code;

    private String message;

    private SystemCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String  code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
