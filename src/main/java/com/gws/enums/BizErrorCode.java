package com.gws.enums;

/**
 *【错误码枚举】
 * 业务错误状态码有6位，都是以40开头，中间两位表未业务（01：游戏，05：用户）
 *
 * @author
 */
public enum BizErrorCode implements CodeStatus {


    PARM_ERROR("400101", "参数非法"),
    SEND_FAIL("400102","消息发送失败"),
    SAVE_FAIL("400103","消息保存失败"),
    NO_MORE_MESSAGE("400104","没有更多消息了"),
    ;
    private String code;
    private String message;

    private BizErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
