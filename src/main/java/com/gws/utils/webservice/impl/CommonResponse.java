package com.gws.utils.webservice.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * api请求统一响应数据类型
 * Created by wangdong on 7/18/16.
 */
public class CommonResponse implements Serializable {

    private Long requestId;

    private String code;

    private String message;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

}
