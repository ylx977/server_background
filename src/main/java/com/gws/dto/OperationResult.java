package com.gws.dto;

import com.gws.enums.CodeStatus;

/**
 * 【操作结果】
 *
 * @author wangdong  26/04/2017.
 */
public class OperationResult<T> {

    private Boolean succ = true;

    private CodeStatus errorCode;

    private String code;

    private String message;

    private T entity;

    public OperationResult(T entity) {
        this.entity = entity;
    }

    public OperationResult(CodeStatus errorCode) {
        this.succ = false;
        this.errorCode = errorCode;
    }

    public OperationResult(String code, String message) {
        this.succ = false;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSucc() {
        return succ;
    }

    public CodeStatus getErrorCode() {
        return errorCode;
    }

    public T getEntity() {
        return entity;
    }
}
