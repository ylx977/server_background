package com.gws.dto;

import com.gws.enums.CodeStatus;

/**
 * 【保存操作结果】
 *
 * @author wangdong  26/04/2017.
 */
public class SaveResult <T> {

    private Boolean succ = true;

    private CodeStatus errorCode;

    private T entity;

    public SaveResult(T entity) {
        this.entity = entity;
    }

    public SaveResult(CodeStatus errorCode) {
        this.succ = false;
        this.errorCode = errorCode;
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
